package com.keycap.keycapdesign.dto.payment;

import com.keycap.keycapdesign.enums.PaymentStatus;

public class VnPayReturnResponse {
    private Long orderId;
    private String orderCode;
    private String responseCode;
    private String transactionNo;
    private boolean success;
    private PaymentStatus paymentStatus;

    public VnPayReturnResponse(Long orderId, String orderCode, String responseCode, String transactionNo,
                               boolean success, PaymentStatus paymentStatus) {
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.responseCode = responseCode;
        this.transactionNo = transactionNo;
        this.success = success;
        this.paymentStatus = paymentStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public boolean isSuccess() {
        return success;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
}
