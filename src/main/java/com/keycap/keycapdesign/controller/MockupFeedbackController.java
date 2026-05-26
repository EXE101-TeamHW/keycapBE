package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.feedback.MockupFeedbackResponse;
import com.keycap.keycapdesign.entity.Mockup;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.MockupRepository;
import com.keycap.keycapdesign.security.CurrentUserService;
import com.keycap.keycapdesign.service.FeedbackService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.exception.UnauthorizedException;

import java.util.List;

@RestController
@RequestMapping("/api/mockups")
public class MockupFeedbackController {
    private final FeedbackService feedbackService;
    private final MockupRepository mockupRepository;
    private final CurrentUserService currentUserService;

    public MockupFeedbackController(FeedbackService feedbackService, MockupRepository mockupRepository,
            CurrentUserService currentUserService) {
        this.feedbackService = feedbackService;
        this.mockupRepository = mockupRepository;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/{id}/feedback")
    public ApiResponse<List<MockupFeedbackResponse>> list(@PathVariable Long id) {
        if (currentUserService.getCurrentUser().getRole() == Role.CUSTOMER) {
            Mockup mockup = mockupRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Mockup not found"));
            Long ownerId = mockup.getTicket().getRequest().getUser().getId();
            if (!currentUserService.getCurrentUserId().equals(ownerId)) {
                throw new UnauthorizedException("Access denied");
            }
        }
        return ApiResponse.success(feedbackService.listByMockup(id));
    }
}
