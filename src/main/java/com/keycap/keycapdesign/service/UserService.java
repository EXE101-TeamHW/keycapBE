package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.user.UserResponse;
import com.keycap.keycapdesign.dto.user.UserStatusUpdateRequest;
import com.keycap.keycapdesign.dto.user.UserRoleUpdateRequest;
import com.keycap.keycapdesign.dto.user.UserProfileUpdateRequest;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getPhone(),
                        user.getAvatarUrl(), user.getRole(), user.getStatus(), user.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public UserResponse updateStatus(Long userId, UserStatusUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setStatus(request.getStatus());
        userRepository.save(user);
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getPhone(), user.getAvatarUrl(),
                user.getRole(), user.getStatus(), user.getCreatedAt());
    }

    public UserResponse updateRole(Long userId, UserRoleUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(request.getRole());
        userRepository.save(user);
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getPhone(), user.getAvatarUrl(),
                user.getRole(), user.getStatus(), user.getCreatedAt());
    }

    public UserResponse updateProfile(Long userId, UserProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
        userRepository.save(user);
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getPhone(), user.getAvatarUrl(),
                user.getRole(), user.getStatus(), user.getCreatedAt());
    }
}
