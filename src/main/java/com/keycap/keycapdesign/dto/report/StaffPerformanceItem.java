package com.keycap.keycapdesign.dto.report;

public class StaffPerformanceItem {
    private Long staffId;
    private String staffEmail;
    private Long totalTickets;

    public StaffPerformanceItem(Long staffId, String staffEmail, Long totalTickets) {
        this.staffId = staffId;
        this.staffEmail = staffEmail;
        this.totalTickets = totalTickets;
    }

    public Long getStaffId() {
        return staffId;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public Long getTotalTickets() {
        return totalTickets;
    }
}

