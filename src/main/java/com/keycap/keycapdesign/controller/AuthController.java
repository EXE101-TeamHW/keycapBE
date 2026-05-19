package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.auth.AuthResponse;
import com.keycap.keycapdesign.dto.auth.LoginRequest;
import com.keycap.keycapdesign.dto.auth.RegisterRequest;
import com.keycap.keycapdesign.dto.auth.ResendVerificationRequest;
import com.keycap.keycapdesign.dto.auth.VerifyEmailRequest;
import com.keycap.keycapdesign.dto.user.UserResponse;
import com.keycap.keycapdesign.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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
    public ApiResponse<UserResponse> me(@RequestParam Long userId) {
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
}

