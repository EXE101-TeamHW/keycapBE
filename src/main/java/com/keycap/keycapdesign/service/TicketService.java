package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.ticket.TicketAssignRequest;
import com.keycap.keycapdesign.dto.ticket.TicketResponse;
import com.keycap.keycapdesign.dto.ticket.TicketStatusUpdateRequest;
import com.keycap.keycapdesign.dto.ticket.StaffTicketListItemResponse;
import com.keycap.keycapdesign.entity.Ticket;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.entity.Order;
import com.keycap.keycapdesign.enums.OrderStatus;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.enums.TicketStatus;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.TicketRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import com.keycap.keycapdesign.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TicketService {
    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository,
                         OrderRepository orderRepository,
                         org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public List<TicketResponse> listTickets() {
        long startedAt = System.nanoTime();
        List<TicketResponse> result = toResponses(ticketRepository.findAllByOrderByCreatedAtDesc());
        logPerformance("all", result.size(), startedAt);
        return result;
    }

    public List<TicketResponse> listByUser(Long userId) {
        long startedAt = System.nanoTime();
        List<TicketResponse> result = toResponses(ticketRepository.findByRequestUserIdOrderByCreatedAtDesc(userId));
        logPerformance("user", result.size(), startedAt);
        return result;
    }

    public Page<TicketResponse> listTickets(Pageable pageable) {
        long startedAt = System.nanoTime();
        Page<Ticket> tickets = ticketRepository.findAllWithDetails(pageable);
        Page<TicketResponse> result = new PageImpl<>(toResponses(tickets.getContent()), pageable, tickets.getTotalElements());
        logPerformance("paged", result.getNumberOfElements(), startedAt);
        return result;
    }

    public Page<TicketResponse> listByUser(Long userId, Pageable pageable) {
        long startedAt = System.nanoTime();
        Page<Ticket> tickets = ticketRepository.findByRequestUserId(userId, pageable);
        Page<TicketResponse> result = new PageImpl<>(toResponses(tickets.getContent()), pageable, tickets.getTotalElements());
        logPerformance("user-paged", result.getNumberOfElements(), startedAt);
        return result;
    }

    public Page<StaffTicketListItemResponse> listStaffTickets(Pageable pageable) {
        long startedAt = System.nanoTime();
        Page<Ticket> tickets = ticketRepository.findAllWithDetails(pageable);
        List<Long> ticketIds = tickets.getContent().stream().map(Ticket::getId).toList();
        Map<Long, Order> ordersByTicketId = ticketIds.isEmpty() ? Map.of()
                : orderRepository.findByTicketIdIn(ticketIds).stream()
                .filter(order -> order.getTicket() != null)
                .collect(Collectors.toMap(order -> order.getTicket().getId(), Function.identity(),
                        (left, right) -> left));
        List<StaffTicketListItemResponse> content = tickets.getContent().stream()
                .map(ticket -> {
                    User customer = ticket.getRequest().getUser();
                    Order order = ordersByTicketId.get(ticket.getId());
                    return new StaffTicketListItemResponse(
                            ticket.getId(), ticket.getTicketCode(), customer.getId(), customer.getFullName(),
                            customer.getEmail(), customer.getPhone(), customer.getBankAccount(),
                            ticket.getRequest().getDesignName(), ticket.getRequest().getReferenceImagesJson(),
                            ticket.getRequest().getNotes(),
                            ticket.getRequest().getLayout() == null ? null : ticket.getRequest().getLayout().name(),
                            ticket.getRequest().getTheme() == null ? null : ticket.getRequest().getTheme().name(),
                            ticket.getDeadline(), ticket.getRevisionCount(), ticket.getQuotedPrice(),
                            ticket.getStatus(), ticket.getCreatedAt(), ticket.getUpdatedAt(),
                            ticket.getAssignedStaff() == null ? null : ticket.getAssignedStaff().getId(),
                            ticket.getAssignedStaff() == null ? null : ticket.getAssignedStaff().getFullName(),
                            ticket.getAdmin() == null ? null : ticket.getAdmin().getFullName(),
                            order == null ? null : order.getId(),
                            order == null ? null : order.getOrderCode(),
                            order == null ? null : order.getStatus().name(),
                            order == null ? null : order.getPaymentStatus().name());
                })
                .toList();
        Page<StaffTicketListItemResponse> result = new PageImpl<>(content, pageable, tickets.getTotalElements());
        logPerformance("staff-paged", result.getNumberOfElements(), startedAt);
        return result;
    }

    public TicketResponse getTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        return toResponse(ticket);
    }

    @Transactional
    public TicketResponse assignTicket(Long id, TicketAssignRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new BadRequestException("Cannot assign staff to a cancelled ticket");
        }
        Order order = orderRepository.findByTicketId(ticket.getId()).orElse(null);
        if (order != null && order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Cannot assign staff because the related order is cancelled");
        }
        User staff = userRepository.findById(request.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        User admin = userRepository.findById(request.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        if (staff.getRole() != Role.STAFF) {
            throw new BadRequestException("Assigned user must have STAFF role");
        }
        if (admin.getRole() != Role.ADMIN) {
            throw new BadRequestException("Admin user must have ADMIN role");
        }
        ticket.setAssignedStaff(staff);
        ticket.setAdmin(admin);
        if (request.getDeadline() != null) {
            ticket.setDeadline(request.getDeadline());
        }
        ticket.setStatus(TicketStatus.IN_REVIEW);
        ticketRepository.save(ticket);
        broadcastTicketUpdate(ticket);
        return toResponse(ticket);
    }

    @Transactional
    public TicketResponse updateStatus(Long id, TicketStatusUpdateRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        TicketStatus oldStatus = ticket.getStatus();
        TicketStatus newStatus = request.getStatus();
        ticket.setStatus(newStatus);
        updateDeadlineForStatus(ticket, oldStatus, newStatus);
        ticketRepository.save(ticket);
        broadcastTicketUpdate(ticket);
        return toResponse(ticket);
    }

    @Transactional
    public TicketResponse updateQuotedPrice(Long id, BigDecimal quotedPrice) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new BadRequestException("Cannot update price for a cancelled ticket");
        }
        if (!canUpdateQuotedPrice(ticket.getStatus())) {
            throw new BadRequestException("Cannot update custom price after customer approval");
        }
        Order order = orderRepository.findByTicketId(ticket.getId()).orElse(null);
        if (order != null && (order.getStatus() == OrderStatus.CANCELLED
                || order.getPaymentStatus() == com.keycap.keycapdesign.enums.PaymentStatus.CANCELLED)) {
            throw new BadRequestException("Cannot update price because the related order is cancelled");
        }
        if (quotedPrice == null || quotedPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Quoted price must be greater than 0");
        }
        ticket.setQuotedPrice(quotedPrice);
        ticketRepository.save(ticket);
        broadcastTicketUpdate(ticket);
        return toResponse(ticket);
    }

    private boolean canUpdateQuotedPrice(TicketStatus status) {
        return status == TicketStatus.PENDING
                || status == TicketStatus.IN_REVIEW
                || status == TicketStatus.DESIGNING
                || status == TicketStatus.AWAITING_APPROVAL;
    }

    public void broadcastTicketUpdate(Ticket ticket) {
        messagingTemplate.convertAndSend("/topic/tickets", toResponse(ticket));
    }

    public static void setInitialDeadline(Ticket ticket, String notes) {
        if (notes != null && notes.contains("DỊCH VỤ BỔ SUNG: Vệ sinh & Mod bàn phím")) {
            if (notes.contains("Hình thức: HWShop thu gom tận nhà")) {
                LocalDate pickupDate = parsePickupDate(notes);
                if (pickupDate != null) {
                    ticket.setDeadline(pickupDate);
                    return;
                }
            }
            ticket.setDeadline(LocalDate.now().plusDays(7));
        } else {
            ticket.setDeadline(LocalDate.now().plusDays(5));
        }
    }

    public static LocalDate parsePickupDate(String notes) {
        try {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("Ngày\\s+(\\d{4}-\\d{2}-\\d{2})");
            java.util.regex.Matcher matcher = pattern.matcher(notes);
            if (matcher.find()) {
                return LocalDate.parse(matcher.group(1));
            }
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }

    public static boolean hasCleanModService(Ticket ticket) {
        if (ticket.getRequest() == null) return false;
        String notes = ticket.getRequest().getNotes();
        return notes != null && notes.contains("DỊCH VỤ BỔ SUNG: Vệ sinh & Mod bàn phím");
    }

    public static void updateDeadlineForStatus(Ticket ticket, TicketStatus oldStatus, TicketStatus newStatus) {
        if (newStatus == TicketStatus.DESIGNING) {
            boolean hasService = hasCleanModService(ticket);
            int days = hasService ? 7 : 5;
            ticket.setDeadline(LocalDate.now().plusDays(days));
        } else if (newStatus == TicketStatus.IN_PRODUCTION) {
            ticket.setDeadline(LocalDate.now().plusDays(10));
        }
    }

    private List<TicketResponse> toResponses(List<Ticket> tickets) {
        if (tickets.isEmpty()) {
            return List.of();
        }
        List<Long> ticketIds = tickets.stream().map(Ticket::getId).toList();
        Map<Long, Order> ordersByTicketId = orderRepository.findByTicketIdIn(ticketIds).stream()
                .filter(order -> order.getTicket() != null)
                .collect(Collectors.toMap(order -> order.getTicket().getId(), Function.identity(), (left, right) -> left));
        return tickets.stream()
                .map(ticket -> toResponse(ticket, ordersByTicketId.get(ticket.getId())))
                .toList();
    }

    private TicketResponse toResponse(Ticket ticket) {
        return toResponse(ticket, orderRepository.findByTicketId(ticket.getId()).orElse(null));
    }

    private TicketResponse toResponse(Ticket ticket, Order order) {
        String designName = ticket.getRequest() != null ? ticket.getRequest().getDesignName() : null;
        String refImages = ticket.getRequest() != null ? ticket.getRequest().getReferenceImagesJson() : null;
        Long customerId = ticket.getRequest() != null && ticket.getRequest().getUser() != null
                ? ticket.getRequest().getUser().getId() : null;
        String customerName = ticket.getRequest() != null && ticket.getRequest().getUser() != null
                ? ticket.getRequest().getUser().getFullName() : null;
        String customerEmail = ticket.getRequest() != null && ticket.getRequest().getUser() != null
                ? ticket.getRequest().getUser().getEmail() : null;
        String customerPhone = ticket.getRequest() != null && ticket.getRequest().getUser() != null
                ? ticket.getRequest().getUser().getPhone() : null;
        String assignedStaffName = ticket.getAssignedStaff() != null ? ticket.getAssignedStaff().getFullName() : null;
        String notes = ticket.getRequest() != null ? ticket.getRequest().getNotes() : null;
        String customerBankAccount = ticket.getRequest() != null && ticket.getRequest().getUser() != null
                ? ticket.getRequest().getUser().getBankAccount() : null;
        String layout = ticket.getRequest() != null && ticket.getRequest().getLayout() != null
                ? ticket.getRequest().getLayout().name() : null;
        String theme = ticket.getRequest() != null && ticket.getRequest().getTheme() != null
                ? ticket.getRequest().getTheme().name() : null;

        Long orderId = order != null ? order.getId() : null;
        String orderStatus = order != null ? order.getStatus().name() : null;
        String orderPaymentStatus = order != null ? order.getPaymentStatus().name() : null;

        return new TicketResponse(ticket.getId(), ticket.getTicketCode(), ticket.getRequest().getId(),
                ticket.getAssignedStaff() == null ? null : ticket.getAssignedStaff().getId(),
                ticket.getAdmin() == null ? null : ticket.getAdmin().getId(), ticket.getDeadline(),
                ticket.getRevisionCount(), ticket.getMaxRevisions(), ticket.getStatus(), ticket.getCreatedAt(),
                designName, refImages, customerId, customerName, customerEmail, customerPhone, assignedStaffName,
                notes, customerBankAccount, layout, theme,
                orderId, orderStatus, orderPaymentStatus, ticket.getQuotedPrice());
    }

    private void logPerformance(String operation, int count, long startedAt) {
        long tookMs = (System.nanoTime() - startedAt) / 1_000_000;
        log.info("[PERF] tickets operation={} count={} took={} ms", operation, count, tookMs);
    }
}
