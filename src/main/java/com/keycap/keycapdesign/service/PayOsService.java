package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.payment.PayOsCreatePaymentRequest;
import com.keycap.keycapdesign.dto.payment.PayOsReturnResponse;
import com.keycap.keycapdesign.dto.payment.PayOsWebhookResponse;
import com.keycap.keycapdesign.dto.payment.PaymentUrlResponse;
import com.keycap.keycapdesign.entity.Order;
import com.keycap.keycapdesign.enums.PaymentMethod;
import com.keycap.keycapdesign.enums.PaymentStatus;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.exception.PayOSException;
import vn.payos.model.webhooks.Webhook;
import vn.payos.model.webhooks.WebhookData;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;

@Service
public class PayOsService {
    private static final String PAID_CODE = "00";
    private static final String PAID_STATUS = "PAID";

    @Value("${payos.client-id:}")
    private String clientId;

    @Value("${payos.api-key:}")
    private String apiKey;

    @Value("${payos.checksum-key:}")
    private String checksumKey;

    @Value("${payos.return-url:http://localhost:8080/api/payments/payos/return}")
    private String returnUrl;

    @Value("${payos.cancel-url:http://localhost:8080/api/payments/payos/cancel}")
    private String cancelUrl;

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public PayOsService(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @Transactional
    public PaymentUrlResponse createPaymentUrl(PayOsCreatePaymentRequest request) {
        validateConfig();
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getTotalAmount() == null || order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Order total amount must be greater than zero");
        }

        CreatePaymentLinkRequest paymentRequest = CreatePaymentLinkRequest.builder()
                .orderCode(order.getId())
                .amount(toPayOsAmount(order.getTotalAmount()))
                .description(toDescription(order))
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .build();

        try {
            CreatePaymentLinkResponse response = payOs().paymentRequests().create(paymentRequest);
            order.setPaymentMethod(PaymentMethod.PAYOS);
            order.setPaymentStatus(PaymentStatus.PENDING);
            orderRepository.save(order);
            return new PaymentUrlResponse(response.getCheckoutUrl());
        } catch (PayOSException ex) {
            throw new BadRequestException("Cannot create PayOS payment link: " + ex.getMessage());
        }
    }

    @Transactional
    public PayOsReturnResponse handleReturn(Map<String, String> queryParams) {
        Long orderId = parseOrderCode(queryParams.get("orderCode"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        String status = queryParams.get("status");
        boolean success = PAID_STATUS.equalsIgnoreCase(status);
        if (success && order.getStatus() == com.keycap.keycapdesign.enums.OrderStatus.PENDING) {
            order.setPaymentMethod(PaymentMethod.PAYOS);
            order.setPaymentStatus(PaymentStatus.PAID);
            orderRepository.save(order);
        } else if (!success && "CANCELLED".equalsIgnoreCase(status) && order.getStatus() == com.keycap.keycapdesign.enums.OrderStatus.PENDING) {
            order.setStatus(com.keycap.keycapdesign.enums.OrderStatus.CANCELLED);
            orderService.restoreStock(order);
            orderRepository.save(order);
        }
        
        return new PayOsReturnResponse(order.getId(),
                order.getOrderCode(),
                queryParams.get("id"),
                status,
                success,
                order.getPaymentStatus());
    }

    @Transactional
    public PayOsWebhookResponse handleWebhook(Webhook webhook) {
        validateConfig();
        try {
            WebhookData data = payOs().webhooks().verify(webhook);
            Long orderId = data.getOrderCode();
            Optional<Order> orderOptional = orderRepository.findById(orderId);
            if (orderOptional.isEmpty()) {
                return new PayOsWebhookResponse(orderId,
                        null,
                        String.valueOf(orderId),
                        data.getPaymentLinkId(),
                        data.getReference(),
                        false,
                        null);
            }

            Order order = orderOptional.get();
            String code = data.getCode();
            boolean success = PAID_CODE.equals(code);
            if (success) {
                order.setPaymentMethod(PaymentMethod.PAYOS);
                order.setPaymentStatus(PaymentStatus.PAID);
                orderRepository.save(order);
            } else if (order.getStatus() == com.keycap.keycapdesign.enums.OrderStatus.PENDING) {
                order.setStatus(com.keycap.keycapdesign.enums.OrderStatus.CANCELLED);
                orderService.restoreStock(order);
                orderRepository.save(order);
            }

            return new PayOsWebhookResponse(order.getId(),
                    order.getOrderCode(),
                    String.valueOf(orderId),
                    data.getPaymentLinkId(),
                    data.getReference(),
                    success,
                    order.getPaymentStatus());
        } catch (BadRequestException | ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BadRequestException("Invalid PayOS webhook: " + ex.getMessage());
        }
    }

    private PayOS payOs() {
        return new PayOS(clientId, apiKey, checksumKey);
    }

    private void validateConfig() {
        if (clientId == null || clientId.isBlank()
                || apiKey == null || apiKey.isBlank()
                || checksumKey == null || checksumKey.isBlank()) {
            throw new BadRequestException("PayOS config is missing");
        }
    }

    private Long toPayOsAmount(BigDecimal amount) {
        return amount.setScale(0, RoundingMode.HALF_UP).longValueExact();
    }

    private String toDescription(Order order) {
        return "DH" + order.getId();
    }

    private Long parseOrderCode(String orderCode) {
        if (orderCode == null || orderCode.isBlank()) {
            throw new BadRequestException("Invalid PayOS order code");
        }
        return Long.parseLong(orderCode);
    }
}
