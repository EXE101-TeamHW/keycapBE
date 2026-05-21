package com.keycap.keycapdesign.dto.payment;

import jakarta.validation.constraints.NotNull;

public class PayOsCreatePaymentRequest {
    @NotNull
    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
