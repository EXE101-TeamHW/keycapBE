package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.payment.PayOsCreatePaymentRequest;
import com.keycap.keycapdesign.dto.payment.PayOsReturnResponse;
import com.keycap.keycapdesign.dto.payment.PayOsWebhookResponse;
import com.keycap.keycapdesign.dto.payment.PaymentUrlResponse;
import com.keycap.keycapdesign.dto.order.OrderResponse;
import com.keycap.keycapdesign.security.CurrentUserService;
import com.keycap.keycapdesign.service.OrderService;
import com.keycap.keycapdesign.service.PayOsService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.model.webhooks.Webhook;
import vn.payos.exception.UnauthorizedException;

import java.util.Map;

@RestController
@RequestMapping("/api/payments/payos")
public class PaymentController {
    private final PayOsService payOsService;
    private final OrderService orderService;
    private final CurrentUserService currentUserService;

    public PaymentController(PayOsService payOsService, OrderService orderService,
            CurrentUserService currentUserService) {
        this.payOsService = payOsService;
        this.orderService = orderService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/create-payment")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<PaymentUrlResponse> createPayment(@Valid @RequestBody PayOsCreatePaymentRequest request) {
        OrderResponse order = orderService.getOrder(request.getOrderId());
        if (!currentUserService.getCurrentUserId().equals(order.getUserId())) {
            throw new UnauthorizedException("Access denied");
        }
        return ApiResponse.success(payOsService.createPaymentUrl(request));
    }

    @GetMapping("/return")
    public ApiResponse<PayOsReturnResponse> paymentReturn(@RequestParam Map<String, String> queryParams) {
        return ApiResponse.success(payOsService.handleReturn(queryParams));
    }

    @GetMapping("/cancel")
    public ApiResponse<PayOsReturnResponse> paymentCancel(@RequestParam Map<String, String> queryParams) {
        return ApiResponse.success(payOsService.handleReturn(queryParams));
    }

    @PostMapping("/webhook")
    public ApiResponse<PayOsWebhookResponse> paymentWebhook(@RequestBody Webhook webhook) {
        return ApiResponse.success(payOsService.handleWebhook(webhook));
    }
}
