package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByTicketCode(String ticketCode);
    List<Ticket> findByRequestUserId(Long userId);
    List<Ticket> findByRequestUserIdOrderByCreatedAtDesc(Long userId);
    List<Ticket> findAllByOrderByCreatedAtDesc();
    Optional<Ticket> findByRequestId(Long requestId);
}

