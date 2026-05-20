package com.keycap.keycapdesign.dto.payment;

public class PaymentUrlResponse {
    private String paymentUrl;

    public PaymentUrlResponse(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }
}
