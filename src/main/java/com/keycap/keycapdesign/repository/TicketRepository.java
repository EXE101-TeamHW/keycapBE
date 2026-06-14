package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByTicketCode(String ticketCode);
    List<Ticket> findByRequestUserId(Long userId);

    @EntityGraph(attributePaths = {"request", "request.user", "assignedStaff", "admin"})
    List<Ticket> findByRequestUserIdOrderByCreatedAtDesc(Long userId);

    @EntityGraph(attributePaths = {"request", "request.user", "assignedStaff", "admin"})
    List<Ticket> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"request", "request.user", "assignedStaff", "admin"})
    Page<Ticket> findByRequestUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"request", "request.user", "assignedStaff", "admin"})
    @Query("select t from Ticket t")
    Page<Ticket> findAllWithDetails(Pageable pageable);

    @Query("""
            select new com.keycap.keycapdesign.dto.report.StaffPerformanceItem(
                t.assignedStaff.id, t.assignedStaff.email, count(t)
            )
            from Ticket t
            where t.assignedStaff is not null
            group by t.assignedStaff.id, t.assignedStaff.email
            order by count(t) desc
            """)
    List<com.keycap.keycapdesign.dto.report.StaffPerformanceItem> summarizeStaffPerformance();

    Optional<Ticket> findByRequestId(Long requestId);

    @EntityGraph(attributePaths = {"request"})
    List<Ticket> findByRequestIdIn(List<Long> requestIds);

    @Override
    @EntityGraph(attributePaths = {"request", "request.user", "assignedStaff", "admin"})
    Optional<Ticket> findById(Long id);
}

