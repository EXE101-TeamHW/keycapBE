package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.feedback.MockupFeedbackResponse;
import com.keycap.keycapdesign.service.FeedbackService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mockups")
public class MockupFeedbackController {
    private final FeedbackService feedbackService;

    public MockupFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/{id}/feedback")
    public ApiResponse<List<MockupFeedbackResponse>> list(@PathVariable Long id) {
        return ApiResponse.success(feedbackService.listByMockup(id));
    }
}

