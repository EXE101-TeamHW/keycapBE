package com.keycap.keycapdesign.dto.chat;

import jakarta.validation.constraints.NotNull;

public class ConversationCreateRequest {
    @NotNull
    private Long customerId;

    private Long staffId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
}
