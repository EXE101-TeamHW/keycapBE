package com.keycap.keycapdesign.dto.payment;

import jakarta.validation.constraints.NotNull;

public class VnPayCreatePaymentRequest {
    @NotNull
    private Long orderId;

    private String bankCode;
    private String locale = "vn";

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
