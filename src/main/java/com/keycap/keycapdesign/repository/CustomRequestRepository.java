package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.CustomRequest;
import com.keycap.keycapdesign.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomRequestRepository extends JpaRepository<CustomRequest, Long> {
    List<CustomRequest> findByUserId(Long userId);
    List<CustomRequest> findByUserIdOrderByCreatedAtDesc(Long userId);
    boolean existsByUserIdAndDesignNameIgnoreCaseAndStatusNot(Long userId, String designName, TicketStatus status);
}

