package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.EmailVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode, Long> {
    Optional<EmailVerificationCode> findTopByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<EmailVerificationCode> findByUserIdAndCodeAndUsedAtIsNull(Long userId, String code);
}

