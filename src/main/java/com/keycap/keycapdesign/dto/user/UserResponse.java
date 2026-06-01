package com.keycap.keycapdesign.dto.user;

import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.enums.UserStatus;

import java.time.LocalDateTime;

public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String avatarUrl;
    private String bankAccount;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;

    public UserResponse(Long id, String email, String fullName, String phone, String avatarUrl, String bankAccount, Role role, UserStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.bankAccount = bankAccount;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public Role getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

