package com.keycap.keycapdesign.dto.feedback;

import com.keycap.keycapdesign.enums.FeedbackType;
import jakarta.validation.constraints.NotNull;

public class MockupFeedbackRequest {
    @NotNull
    private Long mockupId;

    private Long userId;

    private FeedbackType type = FeedbackType.COMMENT;

    private String comment;

    private String annotationsJson;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMockupId() {
        return mockupId;
    }

    public void setMockupId(Long mockupId) {
        this.mockupId = mockupId;
    }

    public FeedbackType getType() {
        return type;
    }

    public void setType(FeedbackType type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAnnotationsJson() {
        return annotationsJson;
    }

    public void setAnnotationsJson(String annotationsJson) {
        this.annotationsJson = annotationsJson;
    }
}


