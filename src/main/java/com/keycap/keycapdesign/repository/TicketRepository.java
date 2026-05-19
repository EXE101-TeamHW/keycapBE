package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByTicketCode(String ticketCode);
    List<Ticket> findByRequestUserId(Long userId);
}

