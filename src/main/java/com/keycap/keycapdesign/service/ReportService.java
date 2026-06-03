package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.report.RevenueReportItem;
import com.keycap.keycapdesign.dto.report.StaffPerformanceItem;
import com.keycap.keycapdesign.dto.report.TrendItem;
import com.keycap.keycapdesign.entity.CustomRequest;
import com.keycap.keycapdesign.entity.Order;
import com.keycap.keycapdesign.entity.Ticket;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.repository.CustomRequestRepository;
import com.keycap.keycapdesign.repository.OrderRepository;
import com.keycap.keycapdesign.repository.TicketRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final CustomRequestRepository customRequestRepository;

    public ReportService(OrderRepository orderRepository, TicketRepository ticketRepository,
                         UserRepository userRepository, CustomRequestRepository customRequestRepository) {
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.customRequestRepository = customRequestRepository;
    }

    public List<RevenueReportItem> revenue(LocalDate from, LocalDate to, String groupBy) {
        LocalDateTime fromTime = from.atStartOfDay();
        LocalDateTime toTime = to.plusDays(1).atStartOfDay().minusSeconds(1);
        List<Order> orders = orderRepository.findByCreatedAtBetween(fromTime, toTime);
        DateTimeFormatter formatter = resolveFormatter(groupBy);
        Map<String, BigDecimal> totals = orders.stream()
                .filter(order -> order.getStatus() == com.keycap.keycapdesign.enums.OrderStatus.COMPLETED || 
                                 order.getStatus() == com.keycap.keycapdesign.enums.OrderStatus.DELIVERED)
                .collect(Collectors.groupingBy(
                        order -> order.getCreatedAt().toLocalDate().format(formatter),
                        Collectors.mapping(Order::getTotalAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
        return totals.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new RevenueReportItem(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<StaffPerformanceItem> staffPerformance() {
        Map<Long, Long> ticketCounts = ticketRepository.findAll().stream()
                .filter(ticket -> ticket.getAssignedStaff() != null)
                .collect(Collectors.groupingBy(ticket -> ticket.getAssignedStaff().getId(), Collectors.counting()));
        List<StaffPerformanceItem> result = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : ticketCounts.entrySet()) {
            User staff = userRepository.findById(entry.getKey()).orElse(null);
            String email = staff == null ? "" : staff.getEmail();
            result.add(new StaffPerformanceItem(entry.getKey(), email, entry.getValue()));
        }
        result.sort(Comparator.comparing(StaffPerformanceItem::getTotalTickets).reversed());
        return result;
    }

    public List<TrendItem> trends() {
        List<CustomRequest> requests = customRequestRepository.findAll();
        Map<String, Long> layoutCounts = requests.stream()
                .collect(Collectors.groupingBy(req -> req.getLayout() == null ? "UNKNOWN" : req.getLayout().name(),
                        Collectors.counting()));
        return layoutCounts.entrySet().stream()
                .map(entry -> new TrendItem(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(TrendItem::getTotal).reversed())
                .collect(Collectors.toList());
    }

    private DateTimeFormatter resolveFormatter(String groupBy) {
        if (groupBy == null) {
            return DateTimeFormatter.ISO_DATE;
        }
        return switch (groupBy.toUpperCase()) {
            case "WEEK" -> DateTimeFormatter.ofPattern("YYYY-'W'ww");
            case "MONTH" -> DateTimeFormatter.ofPattern("yyyy-MM");
            default -> DateTimeFormatter.ISO_DATE;
        };
    }
}
