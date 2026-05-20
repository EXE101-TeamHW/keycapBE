package com.keycap.keycapdesign.dto.chat;

import jakarta.validation.constraints.NotNull;

public class CloseConversationRequest {
    @NotNull
    private Long staffId;

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
}
