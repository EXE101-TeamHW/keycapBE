package com.keycap.keycapdesign.config;


import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.AuthProvider;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.enums.UserStatus;
import com.keycap.keycapdesign.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component

public class Initialiazer implements CommandLineRunner {
    @Autowired
    private  UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        User user = new User();
        user.setEmail("tuan01062004kt@gmail.com");
        user.setPassword("tuan162004");

        user.setFullName("Nguyen Dang Tuan");
        user.setPhone("0329400000");
        user.setAvatarUrl("https://res.cloudinary.com/dzj8q3l9c/image/upload/v1700000000/default-avatar.png");
        user.setBankAccount("ACB Bank - 123456789 - NGUYEN DANG TUAN");
        user.setRole(Role.ADMIN);
        user.setStatus(UserStatus.ACTIVE);
        user.setProvider(AuthProvider.LOCAL);
        user.setProviderId(null);
        user.setEmailVerified(true);
        userRepository.save(user);

    }
}
