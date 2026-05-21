package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.payment.PayOsCreatePaymentRequest;
import com.keycap.keycapdesign.dto.payment.PayOsReturnResponse;
import com.keycap.keycapdesign.dto.payment.PayOsWebhookResponse;
import com.keycap.keycapdesign.dto.payment.PaymentUrlResponse;
import com.keycap.keycapdesign.service.PayOsService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.model.webhooks.Webhook;

import java.util.Map;

@RestController
@RequestMapping("/api/payments/payos")
public class PaymentController {
    private final PayOsService payOsService;

    public PaymentController(PayOsService payOsService) {
        this.payOsService = payOsService;
    }

    @PostMapping("/create-payment")
    public ApiResponse<PaymentUrlResponse> createPayment(@Valid @RequestBody PayOsCreatePaymentRequest request) {
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
