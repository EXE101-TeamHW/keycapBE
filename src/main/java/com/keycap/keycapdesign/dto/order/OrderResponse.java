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
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerBankAccount;
    private Long staffId;
    private String staffName;
    private OrderType type;
    private Long ticketId;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private PaymentType paymentType;
    private String shippingAddress;
    private String trackingNumber;
    private String proofImagesJson;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponse> items;
    private Long conversationId;
    private com.keycap.keycapdesign.enums.TicketStatus ticketStatus;
    private java.time.LocalDate deliveryDeadline;
    private Boolean notificationEmailSent;

    public OrderResponse(Long id, String orderCode, Long userId, String customerName, String customerEmail, String customerPhone, String customerBankAccount, 
                         Long staffId, String staffName, OrderType type, Long ticketId, BigDecimal totalAmount,
                         PaymentMethod paymentMethod, PaymentStatus paymentStatus, PaymentType paymentType,
                         String shippingAddress, String trackingNumber, String proofImagesJson, OrderStatus status, LocalDateTime createdAt,
                         List<OrderItemResponse> items, Long conversationId, com.keycap.keycapdesign.enums.TicketStatus ticketStatus,
                         java.time.LocalDate deliveryDeadline) {
        this.id = id;
        this.orderCode = orderCode;
        this.userId = userId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.customerBankAccount = customerBankAccount;
        this.staffId = staffId;
        this.staffName = staffName;
        this.type = type;
        this.ticketId = ticketId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentType = paymentType;
        this.shippingAddress = shippingAddress;
        this.trackingNumber = trackingNumber;
        this.proofImagesJson = proofImagesJson;
        this.status = status;
        this.createdAt = createdAt;
        this.items = items;
        this.conversationId = conversationId;
        this.ticketStatus = ticketStatus;
        this.deliveryDeadline = deliveryDeadline;
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

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getCustomerBankAccount() {
        return customerBankAccount;
    }

    public Long getStaffId() {
        return staffId;
    }

    public String getStaffName() {
        return staffName;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProofImagesJson() {
        return proofImagesJson;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public com.keycap.keycapdesign.enums.TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public java.time.LocalDate getDeliveryDeadline() {
        return deliveryDeadline;
    }

    public Boolean getNotificationEmailSent() {
        return notificationEmailSent;
    }

    public void setNotificationEmailSent(Boolean notificationEmailSent) {
        this.notificationEmailSent = notificationEmailSent;
    }
}

