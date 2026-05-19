package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.mockup.MockupCreateRequest;
import com.keycap.keycapdesign.dto.mockup.MockupResponse;
import com.keycap.keycapdesign.entity.Mockup;
import com.keycap.keycapdesign.entity.Ticket;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.TicketStatus;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.MockupRepository;
import com.keycap.keycapdesign.repository.TicketRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MockupService {
    private final MockupRepository mockupRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public MockupService(MockupRepository mockupRepository, TicketRepository ticketRepository,
                         UserRepository userRepository) {
        this.mockupRepository = mockupRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public MockupResponse create(Long ticketId, MockupCreateRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        User createdBy = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Mockup mockup = new Mockup();
        mockup.setTicket(ticket);
        mockup.setFileUrl(request.getFileUrl());
        mockup.setFileType(request.getFileType());
        mockup.setDescription(request.getDescription());
        mockup.setCreatedBy(createdBy);
        mockup.setVersion(mockupRepository.findByTicketId(ticketId).size() + 1);
        mockupRepository.save(mockup);
        ticket.setStatus(TicketStatus.AWAITING_APPROVAL);
        ticketRepository.save(ticket);
        return toResponse(mockup);
    }

    public List<MockupResponse> listByTicket(Long ticketId) {
        return mockupRepository.findByTicketId(ticketId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private MockupResponse toResponse(Mockup mockup) {
        return new MockupResponse(mockup.getId(), mockup.getTicket().getId(), mockup.getVersion(),
                mockup.getFileUrl(), mockup.getFileType(), mockup.getDescription(), mockup.getStatus(),
                mockup.getCreatedBy() == null ? null : mockup.getCreatedBy().getId(), mockup.getCreatedAt());
    }
}

