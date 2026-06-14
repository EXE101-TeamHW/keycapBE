package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.Conversation;
import com.keycap.keycapdesign.enums.ConversationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @EntityGraph(attributePaths = {"customer", "staff"})
    List<Conversation> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    @EntityGraph(attributePaths = {"customer", "staff"})
    List<Conversation> findByStaffIdOrderByCreatedAtDesc(Long staffId);
    List<Conversation> findByStatusOrderByCreatedAtDesc(ConversationStatus status);

    @EntityGraph(attributePaths = {"customer", "staff"})
    List<Conversation> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"customer", "staff"})
    Page<Conversation> findByCustomerId(Long customerId, Pageable pageable);

    @EntityGraph(attributePaths = {"customer", "staff"})
    Page<Conversation> findAll(Pageable pageable);

    java.util.Optional<Conversation> findByOrderId(Long orderId);
    java.util.Optional<Conversation> findByTicketId(Long ticketId);
    List<Conversation> findByOrderIdIn(List<Long> orderIds);

    @Override
    @EntityGraph(attributePaths = {"customer", "staff"})
    java.util.Optional<Conversation> findById(Long id);
}
