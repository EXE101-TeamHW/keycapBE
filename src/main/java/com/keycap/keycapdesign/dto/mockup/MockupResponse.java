package com.keycap.keycapdesign.dto.mockup;

import com.keycap.keycapdesign.enums.MockupFileType;
import com.keycap.keycapdesign.enums.MockupStatus;

import java.time.LocalDateTime;

public class MockupResponse {
    private Long id;
    private Long ticketId;
    private Integer version;
    private String fileUrl;
    private MockupFileType fileType;
    private String description;
    private MockupStatus status;
    private Long createdBy;
    private LocalDateTime createdAt;

    public MockupResponse(Long id, Long ticketId, Integer version, String fileUrl, MockupFileType fileType,
                          String description, MockupStatus status, Long createdBy, LocalDateTime createdAt) {
        this.id = id;
        this.ticketId = ticketId;
        this.version = version;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.description = description;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Integer getVersion() {
        return version;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public MockupFileType getFileType() {
        return fileType;
    }

    public String getDescription() {
        return description;
    }

    public MockupStatus getStatus() {
        return status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

