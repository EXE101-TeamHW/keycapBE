package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.config.VNPAYConfig;
import com.keycap.keycapdesign.entity.Order;
import com.keycap.keycapdesign.enums.PaymentStatus;
import com.keycap.keycapdesign.repository.OrderRepository;
import com.keycap.keycapdesign.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;

    public PaymentController(PaymentService paymentService, OrderRepository orderRepository) {
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/create_payment")
    public ApiResponse<String> createPayment(@RequestParam Long orderId, HttpServletRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String paymentUrl = paymentService.createPaymentUrl(order, request);
        return ApiResponse.success(paymentUrl);
    }

    @GetMapping("/vnpay_return")
    public ApiResponse<String> vnpayReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        
        String signValue = VNPAYConfig.hmacSHA512(VNPAYConfig.vnp_HashSecret, hashAllFields(fields));
        
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                // Success
                String orderIdStr = request.getParameter("vnp_TxnRef");
                try {
                    Long orderId = Long.parseLong(orderIdStr);
                    Order order = orderRepository.findById(orderId).orElse(null);
                    if (order != null) {
                        order.setPaymentStatus(PaymentStatus.PAID);
                        orderRepository.save(order);
                    }
                } catch (Exception e) {
                    return ApiResponse.error(500, "Lỗi cập nhật trạng thái đơn hàng", null);
                }
                return ApiResponse.success("Thanh toán thành công");
            } else {
                return ApiResponse.error(400, "Giao dịch thất bại", null);
            }
        } else {
            return ApiResponse.error(400, "Chữ ký không hợp lệ", null);
        }
    }

    private String hashAllFields(Map<String, String> fields) {
        java.util.List<String> fieldNames = new java.util.ArrayList<>(fields.keySet());
        java.util.Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        java.util.Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(java.net.URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
            }
            if (itr.hasNext()) {
                sb.append("&");
            }
        }
        return sb.toString();
    }
}
