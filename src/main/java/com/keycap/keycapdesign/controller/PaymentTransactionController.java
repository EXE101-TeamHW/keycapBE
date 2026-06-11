package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.payment.PaymentTransactionResponse;
import com.keycap.keycapdesign.security.CurrentUserService;
import com.keycap.keycapdesign.service.PaymentTransactionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payment-transactions")
public class PaymentTransactionController {
    private final PaymentTransactionService transactionService;
    private final CurrentUserService currentUserService;

    public PaymentTransactionController(PaymentTransactionService transactionService,
            CurrentUserService currentUserService) {
        this.transactionService = transactionService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<List<PaymentTransactionResponse>> listMyTransactions() {
        return ApiResponse.success(transactionService.listByUser(currentUserService.getCurrentUserId()));
    }
}
