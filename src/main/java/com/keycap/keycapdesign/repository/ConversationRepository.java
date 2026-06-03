package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.Conversation;
import com.keycap.keycapdesign.enums.ConversationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Conversation> findByStaffIdOrderByCreatedAtDesc(Long staffId);
    List<Conversation> findByStatusOrderByCreatedAtDesc(ConversationStatus status);
    List<Conversation> findAllByOrderByCreatedAtDesc();
    java.util.Optional<Conversation> findByOrderId(Long orderId);
}
