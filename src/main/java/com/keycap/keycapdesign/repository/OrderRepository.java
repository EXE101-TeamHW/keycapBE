package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}

