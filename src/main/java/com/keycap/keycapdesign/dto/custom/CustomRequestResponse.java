package com.keycap.keycapdesign.dto.custom;

import com.keycap.keycapdesign.enums.TicketStatus;
import com.keycap.keycapdesign.enums.LayoutType;
import com.keycap.keycapdesign.enums.ProductTheme;

import java.time.LocalDateTime;
import java.util.List;

public class CustomRequestResponse {
    private Long id;
    private Long userId;
    private String designName;
    private LayoutType layout;
    private ProductTheme theme;
    private List<String> referenceImages;
    private String notes;
    private TicketStatus status;
    private LocalDateTime createdAt;

    public CustomRequestResponse(Long id, Long userId, String designName, LayoutType layout, ProductTheme theme,
                                 List<String> referenceImages, String notes, TicketStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.designName = designName;
        this.layout = layout;
        this.theme = theme;
        this.referenceImages = referenceImages;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getDesignName() {
        return designName;
    }

    public LayoutType getLayout() {
        return layout;
    }

    public ProductTheme getTheme() {
        return theme;
    }

    public List<String> getReferenceImages() {
        return referenceImages;
    }

    public String getNotes() {
        return notes;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
