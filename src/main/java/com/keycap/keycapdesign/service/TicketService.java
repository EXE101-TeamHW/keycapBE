package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.ticket.TicketAssignRequest;
import com.keycap.keycapdesign.dto.ticket.TicketResponse;
import com.keycap.keycapdesign.dto.ticket.TicketStatusUpdateRequest;
import com.keycap.keycapdesign.entity.Ticket;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.entity.Order;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.enums.TicketStatus;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.TicketRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import com.keycap.keycapdesign.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {
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
        return ticketRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<TicketResponse> listByUser(Long userId) {
        return ticketRepository.findByRequestUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TicketResponse getTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        return toResponse(ticket);
    }

    public TicketResponse assignTicket(Long id, TicketAssignRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
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

    private TicketResponse toResponse(Ticket ticket) {
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

        Order order = orderRepository.findByTicketId(ticket.getId()).orElse(null);
        Long orderId = order != null ? order.getId() : null;
        String orderStatus = order != null ? order.getStatus().name() : null;
        String orderPaymentStatus = order != null ? order.getPaymentStatus().name() : null;

        return new TicketResponse(ticket.getId(), ticket.getTicketCode(), ticket.getRequest().getId(),
                ticket.getAssignedStaff() == null ? null : ticket.getAssignedStaff().getId(),
                ticket.getAdmin() == null ? null : ticket.getAdmin().getId(), ticket.getDeadline(),
                ticket.getRevisionCount(), ticket.getMaxRevisions(), ticket.getStatus(), ticket.getCreatedAt(),
                designName, refImages, customerId, customerName, customerEmail, customerPhone, assignedStaffName,
                notes, customerBankAccount, layout, theme,
                orderId, orderStatus, orderPaymentStatus);
    }
}
