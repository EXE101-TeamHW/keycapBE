package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);
}


