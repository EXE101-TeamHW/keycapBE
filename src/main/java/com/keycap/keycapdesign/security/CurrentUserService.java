package com.keycap.keycapdesign.security;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.entity.User;

import vn.payos.exception.UnauthorizedException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Authentication required");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User user)
            return user;

        throw new UnauthorizedException("Authentication required");
    }
}
