package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.Order;
import com.keycap.keycapdesign.repository.projection.DashboardAggregateProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "assignedStaff", "ticket"})
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    @EntityGraph(attributePaths = {"user", "assignedStaff", "ticket"})
    List<Order> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"user", "assignedStaff", "ticket"})
    List<Order> findByAssignedStaffIdOrderByCreatedAtDesc(Long staffId);

    @EntityGraph(attributePaths = {"user", "assignedStaff", "ticket"})
    Page<Order> findByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "assignedStaff", "ticket"})
    Page<Order> findByAssignedStaffId(Long staffId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "assignedStaff", "ticket"})
    Page<Order> findAll(Pageable pageable);
    @EntityGraph(attributePaths = {"user", "assignedStaff", "ticket"})
    Page<Order> findByStatus(com.keycap.keycapdesign.enums.OrderStatus status, Pageable pageable);

    List<Order> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    @EntityGraph(attributePaths = {"user", "assignedStaff", "ticket"})
    Optional<Order> findByTicketId(Long ticketId);

    @EntityGraph(attributePaths = {"ticket"})
    List<Order> findByTicketIdIn(List<Long> ticketIds);

    @Override
    @EntityGraph(attributePaths = {"user", "assignedStaff", "ticket"})
    Optional<Order> findById(Long id);

    @Query(value = """
            select status, count(*)
            from orders
            where created_at between :from and :to
            group by status
            """, nativeQuery = true)
    List<Object[]> summarizeStatus(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query(value = """
            select
              count(*) filter (where status in ('COMPLETED','DELIVERED')) as "successfulOrders",
              count(*) filter (where status = 'CANCELLED') as "cancelledOrders",
              count(*) filter (where status in ('PENDING','CONFIRMED','PROCESSING','SHIPPING','SHIPPED')) as "activeOrders",
              count(*) as "totalOrders",
              count(*) filter (where type = 'SHOP') as "shopOrders",
              count(*) filter (where type = 'CUSTOM') as "customOrders",
              count(*) filter (where type = 'CUSTOM' and payment_status in ('PAID','REFUNDED')) as "collectedDepositOrders",
              count(*) filter (where type = 'CUSTOM' and payment_status = 'PAID' and status <> 'CANCELLED') as "heldDepositOrders",
              count(*) filter (where type = 'CUSTOM' and payment_status = 'PAID' and status = 'CANCELLED') as "pendingRefundOrders",
              count(*) filter (where type = 'CUSTOM' and payment_status = 'REFUNDED') as "refundedDepositOrders",
              coalesce(sum(total_amount) filter (where status in ('COMPLETED','DELIVERED')), 0) as "totalRevenue",
              coalesce(sum(total_amount) filter (where type = 'CUSTOM' and payment_status in ('PAID','REFUNDED')), 0) as "totalCollectedDeposits",
              coalesce(sum(total_amount) filter (where type = 'CUSTOM' and payment_status = 'PAID' and status <> 'CANCELLED'), 0) as "totalHeldDeposits",
              coalesce(sum(total_amount) filter (where type = 'CUSTOM' and payment_status = 'PAID' and status = 'CANCELLED'), 0) as "totalPendingRefunds",
              coalesce(sum(total_amount) filter (where type = 'CUSTOM' and payment_status = 'REFUNDED'), 0) as "totalRefundedDeposits"
            from orders
            where created_at between :from and :to
            """, nativeQuery = true)
    DashboardAggregateProjection summarizeDashboard(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query(value = """
            select case
                     when upper(:groupBy) = 'MONTH' then to_char(created_at, 'YYYY-MM')
                     when upper(:groupBy) = 'WEEK' then to_char(created_at, 'IYYY-"W"IW')
                     else to_char(created_at, 'YYYY-MM-DD')
                   end as label,
                   coalesce(sum(total_amount), 0) as total
            from orders
            where created_at between :from and :to
              and status in ('COMPLETED','DELIVERED')
            group by label
            order by label
            """, nativeQuery = true)
    List<Object[]> aggregateRevenue(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to,
                                    @Param("groupBy") String groupBy);
}

