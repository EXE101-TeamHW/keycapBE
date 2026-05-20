package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.payment.PaymentUrlResponse;
import com.keycap.keycapdesign.dto.payment.VnPayCreatePaymentRequest;
import com.keycap.keycapdesign.dto.payment.VnPayReturnResponse;
import com.keycap.keycapdesign.entity.Order;
import com.keycap.keycapdesign.enums.PaymentMethod;
import com.keycap.keycapdesign.enums.PaymentStatus;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

@Service
public class VnPayService {
    private static final DateTimeFormatter VNPAY_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final ZoneId VNPAY_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    @Value("${vnpay.pay-url:https://sandbox.vnpayment.vn/paymentv2/vpcpay.html}")
    private String payUrl;

    @Value("${vnpay.return-url:http://localhost:8080/api/payments/vnpay/return}")
    private String returnUrl;

    @Value("${vnpay.tmn-code:}")
    private String tmnCode;

    @Value("${vnpay.hash-secret:}")
    private String hashSecret;

    private final OrderRepository orderRepository;

    public VnPayService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public PaymentUrlResponse createPaymentUrl(VnPayCreatePaymentRequest request, String ipAddress) {
        validateConfig();
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getTotalAmount() == null || order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Order total amount must be greater than zero");
        }

        order.setPaymentMethod(PaymentMethod.VNPAY);
        order.setPaymentStatus(PaymentStatus.PENDING);
        orderRepository.save(order);

        LocalDateTime now = LocalDateTime.now(VNPAY_ZONE);
        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_Amount", toVnPayAmount(order.getTotalAmount()));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", order.getId() + "-" + System.currentTimeMillis());
        params.put("vnp_OrderInfo", "Thanh toan don hang " + order.getOrderCode());
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", request.getLocale() == null ? "vn" : request.getLocale());
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", ipAddress);
        params.put("vnp_CreateDate", now.format(VNPAY_TIME_FORMAT));
        params.put("vnp_ExpireDate", now.plusMinutes(15).format(VNPAY_TIME_FORMAT));
        if (request.getBankCode() != null && !request.getBankCode().isBlank()) {
            params.put("vnp_BankCode", request.getBankCode());
        }

        String query = buildQuery(params);
        String secureHash = hmacSha512(hashSecret, query);
        return new PaymentUrlResponse(payUrl + "?" + query + "&vnp_SecureHash=" + secureHash);
    }

    @Transactional
    public VnPayReturnResponse handleReturn(Map<String, String> queryParams) {
        validateConfig();
        String receivedHash = queryParams.get("vnp_SecureHash");
        if (receivedHash == null || receivedHash.isBlank()) {
            throw new BadRequestException("Missing VNPAY secure hash");
        }

        Map<String, String> params = new TreeMap<>(queryParams);
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");
        String expectedHash = hmacSha512(hashSecret, buildQuery(params));
        if (!expectedHash.equalsIgnoreCase(receivedHash)) {
            throw new BadRequestException("Invalid VNPAY secure hash");
        }

        Long orderId = parseOrderId(params.get("vnp_TxnRef"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        boolean success = "00".equals(params.get("vnp_ResponseCode"))
                && "00".equals(params.get("vnp_TransactionStatus"));
        if (success) {
            order.setPaymentStatus(PaymentStatus.PAID);
            orderRepository.save(order);
        }

        return new VnPayReturnResponse(order.getId(),
                order.getOrderCode(),
                params.get("vnp_ResponseCode"),
                params.get("vnp_TransactionNo"),
                success,
                order.getPaymentStatus());
    }

    private void validateConfig() {
        if (tmnCode == null || tmnCode.isBlank() || hashSecret == null || hashSecret.isBlank()) {
            throw new BadRequestException("VNPAY sandbox config is missing");
        }
    }

    private String toVnPayAmount(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .toPlainString();
    }

    private Long parseOrderId(String txnRef) {
        if (txnRef == null || txnRef.isBlank()) {
            throw new BadRequestException("Invalid VNPAY transaction reference");
        }
        return Long.parseLong(txnRef.split("-")[0]);
    }

    private String buildQuery(Map<String, String> params) {
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isBlank()) {
                continue;
            }
            if (!query.isEmpty()) {
                query.append('&');
            }
            query.append(encode(entry.getKey())).append('=').append(encode(entry.getValue()));
        }
        return query.toString();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String hmacSha512(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception ex) {
            throw new BadRequestException("Cannot sign VNPAY request");
        }
    }
}
