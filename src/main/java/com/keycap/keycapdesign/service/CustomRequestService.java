package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.custom.CustomRequestCreateRequest;
import com.keycap.keycapdesign.dto.custom.CustomRequestResponse;
import com.keycap.keycapdesign.entity.CustomRequest;
import com.keycap.keycapdesign.entity.Ticket;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.TicketStatus;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.CustomRequestRepository;
import com.keycap.keycapdesign.repository.TicketRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import com.keycap.keycapdesign.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomRequestService {
    private final CustomRequestRepository customRequestRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public CustomRequestService(CustomRequestRepository customRequestRepository, UserRepository userRepository,
                                TicketRepository ticketRepository) {
        this.customRequestRepository = customRequestRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    public CustomRequestResponse create(CustomRequestCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CustomRequest customRequest = new CustomRequest();
        customRequest.setUser(user);
        customRequest.setDesignName(request.getDesignName());
        customRequest.setLayout(request.getLayout());
        customRequest.setTheme(request.getTheme());
        customRequest.setReferenceImagesJson(JsonUtil.toJson(request.getReferenceImages()));
        customRequest.setNotes(request.getNotes());
        customRequest.setStatus(TicketStatus.PENDING);
        customRequestRepository.save(customRequest);

        Ticket ticket = new Ticket();
        ticket.setRequest(customRequest);
        ticket.setTicketCode(generateTicketCode());
        ticket.setStatus(TicketStatus.PENDING);
        ticketRepository.save(ticket);

        return toResponse(customRequest, ticket.getId());
    }

    public List<CustomRequestResponse> listByUser(Long userId) {
        return customRequestRepository.findByUserId(userId).stream()
                .map(req -> {
                    Ticket ticket = ticketRepository.findByRequestId(req.getId()).orElse(null);
                    return toResponse(req, ticket != null ? ticket.getId() : null);
                })
                .collect(Collectors.toList());
    }

    public CustomRequestResponse getById(Long id) {
        CustomRequest customRequest = customRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Custom request not found"));
        Ticket ticket = ticketRepository.findByRequestId(customRequest.getId()).orElse(null);
        return toResponse(customRequest, ticket != null ? ticket.getId() : null);
    }

    private CustomRequestResponse toResponse(CustomRequest customRequest, Long ticketId) {
        return new CustomRequestResponse(customRequest.getId(), ticketId, customRequest.getUser().getId(),
                customRequest.getDesignName(), customRequest.getLayout(), customRequest.getTheme(),
                JsonUtil.fromJson(customRequest.getReferenceImagesJson()), customRequest.getNotes(),
                customRequest.getStatus(), customRequest.getCreatedAt());
    }

    private String generateTicketCode() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return "KC-" + date + "-" + UUID.randomUUID().toString().substring(0, 3).toUpperCase();
    }
}

