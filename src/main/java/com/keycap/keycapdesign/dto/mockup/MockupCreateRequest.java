package com.keycap.keycapdesign.dto.mockup;

import com.keycap.keycapdesign.enums.MockupFileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MockupCreateRequest {
    @NotNull
    private Long createdBy;

    @NotBlank
    private String fileUrl;

    private MockupFileType fileType = MockupFileType.IMAGE;

    private String description;

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public MockupFileType getFileType() {
        return fileType;
    }

    public void setFileType(MockupFileType fileType) {
        this.fileType = fileType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

