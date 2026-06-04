package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.auth.*;
import com.keycap.keycapdesign.entity.EmailVerificationCode;
import com.keycap.keycapdesign.dto.user.UserResponse;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.AuthProvider;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.enums.UserStatus;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.EmailVerificationCodeRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import com.keycap.keycapdesign.security.JwtService;
import com.keycap.keycapdesign.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailVerificationCodeRepository codeRepository;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                       EmailVerificationCodeRepository codeRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.codeRepository = codeRepository;
        this.emailService = emailService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setRole(Role.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        user.setProvider(AuthProvider.LOCAL);
        user.setEmailVerified(false);
        userRepository.save(user);
        sendVerificationCode(user);
        return new AuthResponse(user.getId(), user.getEmail(), user.getRole(), null);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }
        if (user.getProvider() == AuthProvider.LOCAL && Boolean.FALSE.equals(user.getEmailVerified())) {
            throw new BadRequestException("Email is not verified");
        }
        return new AuthResponse(user.getId(), user.getEmail(), user.getRole(), jwtService.generateToken(user));
    }

    public UserResponse me(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getPhone(), user.getAvatarUrl(),
                user.getBankAccount(), user.getRole(), user.getStatus(), user.getCreatedAt());
    }

    public AuthResponse verifyEmail(VerifyEmailRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        EmailVerificationCode code = codeRepository.findByUserIdAndCodeAndUsedAtIsNull(user.getId(), request.getCode())
                .orElseThrow(() -> new BadRequestException("Invalid verification code"));
        if (code.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Verification code expired");
        }
        code.setUsedAt(LocalDateTime.now());
        codeRepository.save(code);
        user.setEmailVerified(true);
        userRepository.save(user);
        return new AuthResponse(user.getId(), user.getEmail(), user.getRole(), jwtService.generateToken(user));
    }

    public void resendVerification(ResendVerificationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new BadRequestException("Email already verified");
        }
        sendVerificationCode(user);
    }

    private void sendVerificationCode(User user) {
        String codeValue = generateCode();
        EmailVerificationCode code = new EmailVerificationCode();
        code.setUser(user);
        code.setCode(codeValue);
        code.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        codeRepository.save(code);
        emailService.sendVerificationCode(user.getEmail(), codeValue);
    }

    private String generateCode() {
        int value = new SecureRandom().nextInt(900000) + 100000;
        return String.valueOf(value);
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email không tồn tại trong hệ thống"));
        
        String codeValue = generateCode();
        EmailVerificationCode code = new EmailVerificationCode();
        code.setUser(user);
        code.setCode(codeValue);
        code.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        codeRepository.save(code);
        
        emailService.sendForgotPasswordCode(user.getEmail(), codeValue);
    }

    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email không tồn tại trong hệ thống"));
        
        EmailVerificationCode code = codeRepository.findByUserIdAndCodeAndUsedAtIsNull(user.getId(), request.getCode())
                .orElseThrow(() -> new BadRequestException("Mã xác minh không chính xác"));
                
        if (code.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Mã xác minh đã hết hạn");
        }
        
        code.setUsedAt(LocalDateTime.now());
        codeRepository.save(code);
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setEmailVerified(true);
        userRepository.save(user);
    }
}


