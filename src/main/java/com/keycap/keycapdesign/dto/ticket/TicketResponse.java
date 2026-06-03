package com.keycap.keycapdesign.dto.ticket;

import com.keycap.keycapdesign.enums.TicketStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TicketResponse {
    private Long id;
    private String ticketCode;
    private Long requestId;
    private Long assignedStaffId;
    private Long adminId;
    private LocalDate deadline;
    private Integer revisionCount;
    private Integer maxRevisions;
    private TicketStatus status;
    private LocalDateTime createdAt;
    private String requestDesignName;
    private String referenceImagesJson;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String assignedStaffName;
    private String notes;
    private String customerBankAccount;
    private String layout;
    private String theme;
    private Long orderId;
    private String orderStatus;
    private String orderPaymentStatus;

    public TicketResponse(Long id, String ticketCode, Long requestId, Long assignedStaffId, Long adminId,
                          LocalDate deadline, Integer revisionCount, Integer maxRevisions, TicketStatus status,
                          LocalDateTime createdAt, String requestDesignName, String referenceImagesJson, 
                          Long customerId, String customerName, String customerEmail, String customerPhone, String assignedStaffName,
                          String notes, String customerBankAccount, String layout, String theme,
                          Long orderId, String orderStatus, String orderPaymentStatus) {
        this.id = id;
        this.ticketCode = ticketCode;
        this.requestId = requestId;
        this.assignedStaffId = assignedStaffId;
        this.adminId = adminId;
        this.deadline = deadline;
        this.revisionCount = revisionCount;
        this.maxRevisions = maxRevisions;
        this.status = status;
        this.createdAt = createdAt;
        this.requestDesignName = requestDesignName;
        this.referenceImagesJson = referenceImagesJson;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.assignedStaffName = assignedStaffName;
        this.notes = notes;
        this.customerBankAccount = customerBankAccount;
        this.layout = layout;
        this.theme = theme;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderPaymentStatus = orderPaymentStatus;
    }

    public Long getId() { return id; }
    public String getTicketCode() { return ticketCode; }
    public Long getRequestId() { return requestId; }
    public Long getAssignedStaffId() { return assignedStaffId; }
    public Long getAdminId() { return adminId; }
    public LocalDate getDeadline() { return deadline; }
    public Integer getRevisionCount() { return revisionCount; }
    public Integer getMaxRevisions() { return maxRevisions; }
    public TicketStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getRequestDesignName() { return requestDesignName; }
    public String getReferenceImagesJson() { return referenceImagesJson; }
    public Long getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public String getAssignedStaffName() { return assignedStaffName; }
    public String getNotes() { return notes; }
    public String getCustomerBankAccount() { return customerBankAccount; }
    public String getLayout() { return layout; }
    public String getTheme() { return theme; }
    public Long getOrderId() { return orderId; }
    public String getOrderStatus() { return orderStatus; }
    public String getOrderPaymentStatus() { return orderPaymentStatus; }
}
