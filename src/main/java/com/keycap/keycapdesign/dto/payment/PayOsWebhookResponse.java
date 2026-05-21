package com.keycap.keycapdesign.dto.payment;

import com.keycap.keycapdesign.enums.PaymentStatus;

public class PayOsWebhookResponse {
    private Long orderId;
    private String orderCode;
    private String payOsOrderCode;
    private String payOsPaymentLinkId;
    private String reference;
    private boolean success;
    private PaymentStatus paymentStatus;

    public PayOsWebhookResponse(Long orderId, String orderCode, String payOsOrderCode, String payOsPaymentLinkId,
                                String reference, boolean success, PaymentStatus paymentStatus) {
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.payOsOrderCode = payOsOrderCode;
        this.payOsPaymentLinkId = payOsPaymentLinkId;
        this.reference = reference;
        this.success = success;
        this.paymentStatus = paymentStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public String getPayOsOrderCode() {
        return payOsOrderCode;
    }

    public String getPayOsPaymentLinkId() {
        return payOsPaymentLinkId;
    }

    public String getReference() {
        return reference;
    }

    public boolean isSuccess() {
        return success;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
}
