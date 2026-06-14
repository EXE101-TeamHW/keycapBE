package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.report.RevenueReportItem;
import com.keycap.keycapdesign.dto.report.StaffPerformanceItem;
import com.keycap.keycapdesign.dto.report.TrendItem;
import com.keycap.keycapdesign.dto.report.DashboardSummaryResponse;
import com.keycap.keycapdesign.repository.CustomRequestRepository;
import com.keycap.keycapdesign.repository.OrderRepository;
import com.keycap.keycapdesign.repository.TicketRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import com.keycap.keycapdesign.repository.projection.DashboardAggregateProjection;
import com.keycap.keycapdesign.enums.Role;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
public class ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;
    private final CustomRequestRepository customRequestRepository;
    private final UserRepository userRepository;

    public ReportService(OrderRepository orderRepository, TicketRepository ticketRepository,
                         CustomRequestRepository customRequestRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
        this.customRequestRepository = customRequestRepository;
        this.userRepository = userRepository;
    }

    public List<RevenueReportItem> revenue(LocalDate from, LocalDate to, String groupBy) {
        long startedAt = System.nanoTime();
        LocalDateTime fromTime = from.atStartOfDay();
        LocalDateTime toTime = to.plusDays(1).atStartOfDay().minusSeconds(1);
        List<RevenueReportItem> result = orderRepository.aggregateRevenue(fromTime, toTime, groupBy == null ? "DAY" : groupBy)
                .stream()
                .map(row -> new RevenueReportItem(String.valueOf(row[0]), toBigDecimal(row[1])))
                .toList();
        logPerformance("revenue", startedAt);
        return result;
    }

    public List<StaffPerformanceItem> staffPerformance() {
        long startedAt = System.nanoTime();
        List<StaffPerformanceItem> result = ticketRepository.summarizeStaffPerformance();
        logPerformance("staff-performance", startedAt);
        return result;
    }

    public List<TrendItem> trends() {
        long startedAt = System.nanoTime();
        List<TrendItem> result = customRequestRepository.summarizeLayouts();
        logPerformance("trends", startedAt);
        return result;
    }

    public DashboardSummaryResponse dashboardSummary(LocalDate from, LocalDate to) {
        long startedAt = System.nanoTime();
        LocalDateTime fromTime = from.atStartOfDay();
        LocalDateTime toTime = to.plusDays(1).atStartOfDay().minusNanos(1);
        DashboardAggregateProjection aggregate = orderRepository.summarizeDashboard(fromTime, toTime);
        Map<String, Long> statusCounts = new LinkedHashMap<>();
        orderRepository.summarizeStatus(fromTime, toTime).forEach(statusRow ->
                statusCounts.put(String.valueOf(statusRow[0]), ((Number) statusRow[1]).longValue()));
        DashboardSummaryResponse result = new DashboardSummaryResponse(
                userRepository.countByRole(Role.CUSTOMER),
                toLong(aggregate.getSuccessfulOrders()),
                toLong(aggregate.getCancelledOrders()),
                toLong(aggregate.getActiveOrders()),
                toLong(aggregate.getTotalOrders()),
                toLong(aggregate.getShopOrders()),
                toLong(aggregate.getCustomOrders()),
                toLong(aggregate.getCollectedDepositOrders()),
                toLong(aggregate.getHeldDepositOrders()),
                toLong(aggregate.getPendingRefundOrders()),
                toLong(aggregate.getRefundedDepositOrders()),
                toBigDecimal(aggregate.getTotalRevenue()),
                toBigDecimal(aggregate.getTotalCollectedDeposits()),
                toBigDecimal(aggregate.getTotalHeldDeposits()),
                toBigDecimal(aggregate.getTotalPendingRefunds()),
                toBigDecimal(aggregate.getTotalRefundedDeposits()),
                statusCounts);
        logPerformance("summary", startedAt);
        return result;
    }

    private void logPerformance(String operation, long startedAt) {
        log.info("[PERF] admin-dashboard operation={} took={} ms", operation,
                (System.nanoTime() - startedAt) / 1_000_000);
    }

    private long toLong(Object value) {
        return value == null ? 0L : ((Number) value).longValue();
    }

    private BigDecimal toBigDecimal(Object value) {
        return value == null ? BigDecimal.ZERO : new BigDecimal(value.toString());
    }
}
