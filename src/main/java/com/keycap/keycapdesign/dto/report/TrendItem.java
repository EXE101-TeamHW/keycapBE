package com.keycap.keycapdesign.dto.report;

public class TrendItem {
    private String key;
    private Long total;

    public TrendItem(String key, Long total) {
        this.key = key;
        this.total = total;
    }

    public String getKey() {
        return key;
    }

    public Long getTotal() {
        return total;
    }
}

