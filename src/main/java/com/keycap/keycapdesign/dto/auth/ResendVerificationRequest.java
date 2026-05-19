package com.keycap.keycapdesign.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class ResendVerificationRequest {
    @NotBlank
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

