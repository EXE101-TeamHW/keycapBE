package com.keycap.keycapdesign.dto.payment;

public class PaymentUrlResponse {
    private String paymentUrl;
    private Long orderId;

    public PaymentUrlResponse(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public PaymentUrlResponse(String paymentUrl, Long orderId) {
        this.paymentUrl = paymentUrl;
        this.orderId = orderId;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public Long getOrderId() {
        return orderId;
    }
}
