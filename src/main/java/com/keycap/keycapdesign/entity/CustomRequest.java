package com.keycap.keycapdesign.entity;

import com.keycap.keycapdesign.enums.TicketStatus;
import com.keycap.keycapdesign.enums.LayoutType;
import com.keycap.keycapdesign.enums.ProductTheme;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "custom_requests")
public class CustomRequest extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "design_name", nullable = false, columnDefinition = "nvarchar(255)")
    private String designName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "nvarchar(100)")
    private LayoutType layout;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "nvarchar(255)")
    private ProductTheme theme;

    @Column(name = "reference_images", columnDefinition = "nvarchar(max)")
    private String referenceImagesJson;

    @Column(columnDefinition = "nvarchar(max)")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.PENDING;

    @OneToOne(mappedBy = "request")
    private Ticket ticket;
}
