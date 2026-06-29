package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
    List<Review> findAllByOrderByCreatedAtDesc();
    boolean existsByOrderIdAndProductId(Long orderId, Long productId);

    @Query("select r.rating from Review r where r.rating is not null")
    List<Integer> findRatings();
}

