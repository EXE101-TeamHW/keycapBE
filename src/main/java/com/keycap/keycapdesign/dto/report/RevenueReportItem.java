package com.keycap.keycapdesign.dto.report;

import java.math.BigDecimal;

public class RevenueReportItem {
    private String period;
    private BigDecimal totalAmount;

    public RevenueReportItem(String period, BigDecimal totalAmount) {
        this.period = period;
        this.totalAmount = totalAmount;
    }

    public String getPeriod() {
        return period;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}

