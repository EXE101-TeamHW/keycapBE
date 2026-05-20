package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.payment.PaymentUrlResponse;
import com.keycap.keycapdesign.dto.payment.VnPayCreatePaymentRequest;
import com.keycap.keycapdesign.dto.payment.VnPayReturnResponse;
import com.keycap.keycapdesign.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payments/vnpay")
public class PaymentController {
    private final VnPayService vnPayService;

    public PaymentController(VnPayService vnPayService) {
        this.vnPayService = vnPayService;
    }

    @PostMapping("/create-payment")
    public ApiResponse<PaymentUrlResponse> createPayment(@Valid @RequestBody VnPayCreatePaymentRequest request,
                                                         HttpServletRequest httpRequest) {
        return ApiResponse.success(vnPayService.createPaymentUrl(request, getClientIp(httpRequest)));
    }

    @GetMapping("/return")
    public ApiResponse<VnPayReturnResponse> paymentReturn(@RequestParam Map<String, String> queryParams) {
        return ApiResponse.success(vnPayService.handleReturn(queryParams));
    }

    private String getClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
