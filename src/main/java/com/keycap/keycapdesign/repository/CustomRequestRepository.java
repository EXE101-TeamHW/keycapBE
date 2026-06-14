package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.CustomRequest;
import com.keycap.keycapdesign.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomRequestRepository extends JpaRepository<CustomRequest, Long> {
    List<CustomRequest> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user"})
    List<CustomRequest> findByUserIdOrderByCreatedAtDesc(Long userId);
    @EntityGraph(attributePaths = {"user"})
    Page<CustomRequest> findByUserId(Long userId, Pageable pageable);
    boolean existsByUserIdAndDesignNameIgnoreCaseAndStatusNot(Long userId, String designName, TicketStatus status);

    @Query("""
            select new com.keycap.keycapdesign.dto.report.TrendItem(
                coalesce(cast(c.layout as string), 'UNKNOWN'), count(c)
            )
            from CustomRequest c
            group by c.layout
            order by count(c) desc
            """)
    List<com.keycap.keycapdesign.dto.report.TrendItem> summarizeLayouts();
}

