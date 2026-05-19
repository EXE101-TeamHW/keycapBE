package com.keycap.keycapdesign.dto.user;

import com.keycap.keycapdesign.enums.Role;
import jakarta.validation.constraints.NotNull;

public class UserRoleUpdateRequest {
    @NotNull
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

