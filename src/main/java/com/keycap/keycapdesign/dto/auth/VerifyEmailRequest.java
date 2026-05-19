package com.keycap.keycapdesign.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class VerifyEmailRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String code;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

