package com.keycap.keycapdesign.config;

import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.AuthProvider;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.enums.UserStatus;
import com.keycap.keycapdesign.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        initAdminAccount();
        initStaffAccount();
    }

    private void initAdminAccount() {
        String adminEmail = "admin123@gmail.com";
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("Admin123@"));
            admin.setFullName("Admin");
            admin.setRole(Role.ADMIN);
            admin.setProvider(AuthProvider.LOCAL);
            admin.setEmailVerified(true);
            admin.setStatus(UserStatus.ACTIVE);
            userRepository.save(admin);
            log.info("✅ Admin account created: {}", adminEmail);
        } else {
            log.info("ℹ️  Admin account already exists: {}", adminEmail);
        }
    }

    private void initStaffAccount() {
        String staffEmail = "staff@gmail.com";
        if (userRepository.findByEmail(staffEmail).isEmpty()) {
            User staff = new User();
            staff.setEmail(staffEmail);
            staff.setPassword(passwordEncoder.encode("Staff123@"));
            staff.setFullName("Staff");
            staff.setRole(Role.STAFF);
            staff.setProvider(AuthProvider.LOCAL);
            staff.setEmailVerified(true);
            staff.setStatus(UserStatus.ACTIVE);
            userRepository.save(staff);
            log.info("✅ Staff account created: {}", staffEmail);
        } else {
            log.info("ℹ️  Staff account already exists: {}", staffEmail);
        }
    }
}
