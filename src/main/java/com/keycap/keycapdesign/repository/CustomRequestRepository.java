package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.CustomRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomRequestRepository extends JpaRepository<CustomRequest, Long> {
    List<CustomRequest> findByUserId(Long userId);
}

