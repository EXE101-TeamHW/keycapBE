package com.keycap.keycapdesign.dto.feedback;

import com.keycap.keycapdesign.enums.FeedbackType;

import java.time.LocalDateTime;

public class MockupFeedbackResponse {
    private Long id;
    private Long mockupId;
    private Long userId;
    private FeedbackType type;
    private String comment;
    private String annotationsJson;
    private LocalDateTime createdAt;

    public MockupFeedbackResponse(Long id, Long mockupId, Long userId, FeedbackType type, String comment,
                                  String annotationsJson, LocalDateTime createdAt) {
        this.id = id;
        this.mockupId = mockupId;
        this.userId = userId;
        this.type = type;
        this.comment = comment;
        this.annotationsJson = annotationsJson;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getMockupId() {
        return mockupId;
    }

    public Long getUserId() {
        return userId;
    }

    public FeedbackType getType() {
        return type;
    }

    public String getComment() {
        return comment;
    }

    public String getAnnotationsJson() {
        return annotationsJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

