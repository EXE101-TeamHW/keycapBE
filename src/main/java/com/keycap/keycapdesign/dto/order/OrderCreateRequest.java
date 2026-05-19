package com.keycap.keycapdesign.dto.order;

import com.keycap.keycapdesign.enums.OrderType;
import com.keycap.keycapdesign.enums.PaymentMethod;
import com.keycap.keycapdesign.enums.PaymentType;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderCreateRequest {
    @NotNull
    private Long userId;

    private OrderType type = OrderType.SHOP;

    private Long ticketId;

    private PaymentMethod paymentMethod = PaymentMethod.COD;

    private PaymentType paymentType = PaymentType.FULL;

    private String shippingAddress;

    @NotNull
    private List<OrderItemRequest> items;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}

