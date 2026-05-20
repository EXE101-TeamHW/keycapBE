package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.ticket.TicketAssignRequest;
import com.keycap.keycapdesign.dto.ticket.TicketResponse;
import com.keycap.keycapdesign.dto.ticket.TicketStatusUpdateRequest;
import com.keycap.keycapdesign.entity.Ticket;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.enums.TicketStatus;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.TicketRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
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
        ticket.setDeadline(request.getDeadline());
        ticket.setStatus(TicketStatus.IN_REVIEW);
        ticketRepository.save(ticket);
        return toResponse(ticket);
    }

    public TicketResponse updateStatus(Long id, TicketStatusUpdateRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        ticket.setStatus(request.getStatus());
        ticketRepository.save(ticket);
        return toResponse(ticket);
    }

    private TicketResponse toResponse(Ticket ticket) {
        String designName = ticket.getRequest() != null ? ticket.getRequest().getDesignName() : null;
        String refImages = ticket.getRequest() != null ? ticket.getRequest().getReferenceImagesJson() : null;
        return new TicketResponse(ticket.getId(), ticket.getTicketCode(), ticket.getRequest().getId(),
                ticket.getAssignedStaff() == null ? null : ticket.getAssignedStaff().getId(),
                ticket.getAdmin() == null ? null : ticket.getAdmin().getId(), ticket.getDeadline(),
                ticket.getRevisionCount(), ticket.getMaxRevisions(), ticket.getStatus(), ticket.getCreatedAt(),
                designName, refImages);
    }
}
