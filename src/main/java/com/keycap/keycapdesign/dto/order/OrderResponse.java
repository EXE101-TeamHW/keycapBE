package com.keycap.keycapdesign.dto.order;

import com.keycap.keycapdesign.enums.OrderStatus;
import com.keycap.keycapdesign.enums.OrderType;
import com.keycap.keycapdesign.enums.PaymentMethod;
import com.keycap.keycapdesign.enums.PaymentStatus;
import com.keycap.keycapdesign.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private String orderCode;
    private Long userId;
    private OrderType type;
    private Long ticketId;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private PaymentType paymentType;
    private String shippingAddress;
    private String trackingNumber;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;

    public OrderResponse(Long id, String orderCode, Long userId, OrderType type, Long ticketId, BigDecimal totalAmount,
                         PaymentMethod paymentMethod, PaymentStatus paymentStatus, PaymentType paymentType,
                         String shippingAddress, String trackingNumber, OrderStatus status, LocalDateTime createdAt,
                         List<OrderItemResponse> items) {
        this.id = id;
        this.orderCode = orderCode;
        this.userId = userId;
        this.type = type;
        this.ticketId = ticketId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentType = paymentType;
        this.shippingAddress = shippingAddress;
        this.trackingNumber = trackingNumber;
        this.status = status;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public Long getUserId() {
        return userId;
    }

    public OrderType getType() {
        return type;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }
}

