package com.keycap.keycapdesign.dto.ticket;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TicketQuotePriceRequest {
    @NotNull
    @DecimalMin(value = "0.01", message = "Quoted price must be greater than 0")
    private BigDecimal quotedPrice;

    public BigDecimal getQuotedPrice() {
        return quotedPrice;
    }

    public void setQuotedPrice(BigDecimal quotedPrice) {
        this.quotedPrice = quotedPrice;
    }
}
