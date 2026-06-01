package com.keycap.keycapdesign.enums;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PROCESSING,
    SHIPPING,
    SHIPPED, // keeping for backward compatibility if any
    DELIVERED,
    COMPLETED,
    CANCELLED,
    REFUNDED
}

