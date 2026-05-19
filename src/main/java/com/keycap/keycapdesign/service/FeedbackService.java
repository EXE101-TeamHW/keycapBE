package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.feedback.MockupFeedbackRequest;
import com.keycap.keycapdesign.dto.feedback.MockupFeedbackResponse;
import com.keycap.keycapdesign.entity.Mockup;
import com.keycap.keycapdesign.entity.MockupFeedback;
import com.keycap.keycapdesign.entity.Ticket;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.FeedbackType;
import com.keycap.keycapdesign.enums.TicketStatus;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.MockupFeedbackRepository;
import com.keycap.keycapdesign.repository.MockupRepository;
import com.keycap.keycapdesign.repository.TicketRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    private final MockupFeedbackRepository feedbackRepository;
    private final MockupRepository mockupRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public FeedbackService(MockupFeedbackRepository feedbackRepository, MockupRepository mockupRepository,
                           TicketRepository ticketRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.mockupRepository = mockupRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public MockupFeedbackResponse create(Long ticketId, Long mockupId, MockupFeedbackRequest request) {
        Mockup mockup = mockupRepository.findById(mockupId)
                .orElseThrow(() -> new ResourceNotFoundException("Mockup not found"));
        if (ticketId != null && (mockup.getTicket() == null || !ticketId.equals(mockup.getTicket().getId()))) {
            throw new BadRequestException("Mockup does not belong to ticket");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        MockupFeedback feedback = new MockupFeedback();
        feedback.setMockup(mockup);
        feedback.setUser(user);
        feedback.setType(request.getType());
        feedback.setComment(request.getComment());
        feedback.setAnnotationsJson(request.getAnnotationsJson());
        feedbackRepository.save(feedback);

        Ticket ticket = mockup.getTicket();
        if (request.getType() == FeedbackType.REVISION) {
            if (ticket.getRevisionCount() >= ticket.getMaxRevisions()) {
                throw new BadRequestException("Max revisions reached");
            }
            ticket.setRevisionCount(ticket.getRevisionCount() + 1);
            ticket.setStatus(TicketStatus.DESIGNING);
            ticketRepository.save(ticket);
        } else if (request.getType() == FeedbackType.APPROVED) {
            ticket.setStatus(TicketStatus.APPROVED);
            ticketRepository.save(ticket);
        }

        return toResponse(feedback);
    }

    public List<MockupFeedbackResponse> listByMockup(Long mockupId) {
        return feedbackRepository.findByMockupId(mockupId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private MockupFeedbackResponse toResponse(MockupFeedback feedback) {
        return new MockupFeedbackResponse(feedback.getId(), feedback.getMockup().getId(),
                feedback.getUser().getId(), feedback.getType(), feedback.getComment(),
                feedback.getAnnotationsJson(), feedback.getCreatedAt());
    }
}


