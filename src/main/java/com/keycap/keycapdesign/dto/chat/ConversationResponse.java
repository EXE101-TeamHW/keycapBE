package com.keycap.keycapdesign.dto.chat;

import com.keycap.keycapdesign.enums.ConversationStatus;

import java.time.LocalDateTime;

public class ConversationResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long staffId;
    private String staffName;
    private Long orderId;
    private ConversationStatus status;
    private long unreadCount;
    private LocalDateTime createdAt;

    public ConversationResponse(Long id, Long customerId, String customerName, Long staffId, String staffName,
                                Long orderId, ConversationStatus status, long unreadCount, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.staffId = staffId;
        this.staffName = staffName;
        this.orderId = orderId;
        this.status = status;
        this.unreadCount = unreadCount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Long getStaffId() {
        return staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public ConversationStatus getStatus() {
        return status;
    }

    public long getUnreadCount() {
        return unreadCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
