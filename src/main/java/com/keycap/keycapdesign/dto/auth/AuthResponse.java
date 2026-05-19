package com.keycap.keycapdesign.dto.auth;

import com.keycap.keycapdesign.enums.Role;

public class AuthResponse {
    private Long userId;
    private String email;
    private Role role;
    private String token;

    public AuthResponse(Long userId, String email, Role role, String token) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}

