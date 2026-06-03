package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.auth.*;
import com.keycap.keycapdesign.dto.user.UserProfileUpdateRequest;
import com.keycap.keycapdesign.dto.user.UserResponse;
import com.keycap.keycapdesign.security.CurrentUserService;
import com.keycap.keycapdesign.service.AuthService;
import com.keycap.keycapdesign.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final CurrentUserService currentUserService;

    public AuthController(AuthService authService, UserService userService, CurrentUserService currentUserService) {
        this.authService = authService;
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success(authService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> me() {
        long userId = currentUserService.getCurrentUserId();
        return ApiResponse.success(authService.me(userId));
    }

    @PostMapping("/verify")
    public ApiResponse<AuthResponse> verify(@Valid @RequestBody VerifyEmailRequest request) {
        return ApiResponse.success(authService.verifyEmail(request));
    }

    @PostMapping("/resend")
    public ApiResponse<Object> resend(@Valid @RequestBody ResendVerificationRequest request) {
        authService.resendVerification(request);
        return ApiResponse.success(null);
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Object> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ApiResponse.success(null);
    }

    @PostMapping("/reset-password")
    public ApiResponse<Object> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ApiResponse.success(null);
    }

    /**
     * PUT /api/auth/profile/{userId} - Customer cập nhật thông tin cá nhân
     */
    @PutMapping("/profile")
    public ApiResponse<UserResponse> updateProfile(
            @RequestBody UserProfileUpdateRequest request) {
        long userId = currentUserService.getCurrentUserId();
        return ApiResponse.success(userService.updateProfile(userId, request));
    }
}
