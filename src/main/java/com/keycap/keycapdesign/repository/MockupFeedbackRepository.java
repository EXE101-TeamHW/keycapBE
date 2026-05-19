package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.MockupFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MockupFeedbackRepository extends JpaRepository<MockupFeedback, Long> {
    List<MockupFeedback> findByMockupId(Long mockupId);
}

