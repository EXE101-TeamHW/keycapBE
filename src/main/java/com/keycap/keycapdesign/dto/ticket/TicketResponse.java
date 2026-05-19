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

    public TicketResponse(Long id, String ticketCode, Long requestId, Long assignedStaffId, Long adminId,
                          LocalDate deadline, Integer revisionCount, Integer maxRevisions, TicketStatus status,
                          LocalDateTime createdAt) {
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
    }

    public Long getId() {
        return id;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public Long getRequestId() {
        return requestId;
    }

    public Long getAssignedStaffId() {
        return assignedStaffId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public Integer getRevisionCount() {
        return revisionCount;
    }

    public Integer getMaxRevisions() {
        return maxRevisions;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

