package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.Mockup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MockupRepository extends JpaRepository<Mockup, Long> {
    List<Mockup> findByTicketId(Long ticketId);
}

