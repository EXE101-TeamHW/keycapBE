package com.keycap.keycapdesign.dto.ticket;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TicketAssignRequest {
    @NotNull
    private Long adminId;

    @NotNull
    private Long staffId;

    private LocalDate deadline;

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}

