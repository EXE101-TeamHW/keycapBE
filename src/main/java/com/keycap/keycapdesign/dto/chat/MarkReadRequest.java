package com.keycap.keycapdesign.dto.chat;

import jakarta.validation.constraints.NotNull;

public class MarkReadRequest {
    @NotNull
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
