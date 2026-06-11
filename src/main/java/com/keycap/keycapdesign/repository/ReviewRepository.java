package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
    List<Review> findAllByOrderByCreatedAtDesc();
    boolean existsByOrderIdAndProductId(Long orderId, Long productId);
}

