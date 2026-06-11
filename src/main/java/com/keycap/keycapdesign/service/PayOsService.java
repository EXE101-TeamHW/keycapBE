package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.payment.PayOsCreatePaymentRequest;
import com.keycap.keycapdesign.dto.payment.PayOsReturnResponse;
import com.keycap.keycapdesign.dto.payment.PayOsWebhookResponse;
import com.keycap.keycapdesign.dto.payment.PaymentUrlResponse;
import com.keycap.keycapdesign.entity.Order;
import com.keycap.keycapdesign.enums.PaymentMethod;
import com.keycap.keycapdesign.enums.PaymentStatus;
import com.keycap.keycapdesign.enums.TicketStatus;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PayOsService {
    private static final String PAID_CODE = "00";
    private static final String PAID_STATUS = "PAID";
    private static final String CANCELLED_STATUS = "CANCELLED";
    private static final Pattern ORDER_CODE_PATTERN = Pattern.compile("\\d+");

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
    private final PaymentTransactionService paymentTransactionService;

    public PayOsService(OrderRepository orderRepository, OrderService orderService,
            PaymentTransactionService paymentTransactionService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.paymentTransactionService = paymentTransactionService;
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
                .cancelUrl(toCancelUrl(order))
                .build();

        try {
            CreatePaymentLinkResponse response = payOs().paymentRequests().create(paymentRequest);
            order.setPaymentMethod(PaymentMethod.PAYOS);
            order.setPaymentStatus(PaymentStatus.PENDING);
            orderRepository.save(order);
            return new PaymentUrlResponse(response.getCheckoutUrl(), order.getId());
        } catch (PayOSException ex) {
            String message = ex.getMessage() == null ? "" : ex.getMessage();
            if (message.toLowerCase().contains("tồn tại") || message.toLowerCase().contains("exist")) {
                throw new BadRequestException("Đơn thanh toán PayOS đã tồn tại. Vui lòng kiểm tra đơn hàng hoặc thử lại sau.");
            }
            throw new BadRequestException("Không thể tạo link thanh toán PayOS: " + message);
        }
    }

    @Transactional
    public PayOsReturnResponse handleReturn(Map<String, String> queryParams) {
        Long orderId = parseOrderCode(queryParams.get("orderCode"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        String status = queryParams.get("status");
        boolean cancelled = isPayOsCancelled(queryParams);
        boolean success = !cancelled && PAID_STATUS.equalsIgnoreCase(status);
        if (success && order.getStatus() == com.keycap.keycapdesign.enums.OrderStatus.PENDING) {
            order.setPaymentMethod(PaymentMethod.PAYOS);
            order.setPaymentStatus(PaymentStatus.PAID);
            orderRepository.save(order);
            paymentTransactionService.recordPayment(order, order.getTotalAmount(),
                    paymentReference(queryParams.get("id"), order), "HWSHOP qua PayOS", LocalDateTime.now(), false);
        } else if (cancelled && canMarkPaymentCancelled(order)) {
            markPaymentCancelled(order);
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
                paymentTransactionService.recordPayment(order,
                        data.getAmount() == null ? order.getTotalAmount() : BigDecimal.valueOf(data.getAmount()),
                        paymentReference(data.getReference(), order),
                        paymentDestination(data), parseTransactionTime(data.getTransactionDateTime()), true);
            } else if (canMarkPaymentCancelled(order)) {
                markPaymentCancelled(order);
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

    private String toCancelUrl(Order order) {
        String separator = cancelUrl.contains("?") ? "&" : "?";
        String normalizedUrl = cancelUrl;
        if (!normalizedUrl.contains("status=")) {
            normalizedUrl += separator + "status=CANCELLED";
            separator = "&";
        }
        if (!normalizedUrl.contains("orderCode=")) {
            normalizedUrl += separator + "orderCode=" + order.getId();
        }
        return normalizedUrl;
    }

    private Long parseOrderCode(String orderCode) {
        if (orderCode == null || orderCode.isBlank()) {
            throw new BadRequestException("Invalid PayOS order code");
        }
        Matcher matcher = ORDER_CODE_PATTERN.matcher(orderCode);
        if (!matcher.find()) {
            throw new BadRequestException("Invalid PayOS order code");
        }
        return Long.parseLong(matcher.group());
    }

    private boolean isPayOsCancelled(Map<String, String> queryParams) {
        String status = queryParams.get("status");
        String cancel = queryParams.get("cancel");
        return (status != null && status.toUpperCase().contains(CANCELLED_STATUS))
                || "true".equalsIgnoreCase(cancel)
                || "1".equals(cancel);
    }

    private boolean canMarkPaymentCancelled(Order order) {
        return order.getPaymentStatus() != PaymentStatus.PAID
                && order.getPaymentStatus() != PaymentStatus.REFUNDED;
    }

    private void markPaymentCancelled(Order order) {
        boolean wasAlreadyCancelled = order.getStatus() == com.keycap.keycapdesign.enums.OrderStatus.CANCELLED;
        order.setStatus(com.keycap.keycapdesign.enums.OrderStatus.CANCELLED);
        order.setPaymentStatus(PaymentStatus.CANCELLED);
        if (!wasAlreadyCancelled) {
            orderService.restoreStock(order);
        }
        if (order.getTicket() != null) {
            order.getTicket().setStatus(TicketStatus.CANCELLED);
            if (order.getTicket().getRequest() != null) {
                order.getTicket().getRequest().setStatus(TicketStatus.CANCELLED);
            }
        }
        orderRepository.save(order);
    }

    private String paymentReference(String reference, Order order) {
        return reference == null || reference.isBlank() ? "PAYOS-" + order.getOrderCode() : reference;
    }

    private String paymentDestination(WebhookData data) {
        String accountNumber = data.getAccountNumber();
        String accountName = data.getVirtualAccountName();
        StringBuilder destination = new StringBuilder("HWSHOP qua PayOS");
        if (accountName != null && !accountName.isBlank()) {
            destination.append(" - ").append(accountName);
        }
        if (accountNumber != null && !accountNumber.isBlank()) {
            destination.append(" - TK ").append(accountNumber);
        }
        return destination.toString();
    }

    private LocalDateTime parseTransactionTime(String value) {
        if (value == null || value.isBlank()) return LocalDateTime.now();
        for (DateTimeFormatter formatter : new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        }) {
            try {
                return LocalDateTime.parse(value, formatter);
            } catch (DateTimeParseException ignored) {
                // Try the next PayOS-supported timestamp shape.
            }
        }
        return LocalDateTime.now();
    }
}
