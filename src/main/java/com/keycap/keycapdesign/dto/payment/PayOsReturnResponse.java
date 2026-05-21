package com.keycap.keycapdesign.dto.payment;

import com.keycap.keycapdesign.enums.PaymentStatus;

public class PayOsReturnResponse {
    private Long orderId;
    private String orderCode;
    private String payOsPaymentLinkId;
    private String payOsStatus;
    private boolean success;
    private PaymentStatus paymentStatus;

    public PayOsReturnResponse(Long orderId, String orderCode, String payOsPaymentLinkId, String payOsStatus,
                               boolean success, PaymentStatus paymentStatus) {
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.payOsPaymentLinkId = payOsPaymentLinkId;
        this.payOsStatus = payOsStatus;
        this.success = success;
        this.paymentStatus = paymentStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public String getPayOsPaymentLinkId() {
        return payOsPaymentLinkId;
    }

    public String getPayOsStatus() {
        return payOsStatus;
    }

    public boolean isSuccess() {
        return success;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
}
