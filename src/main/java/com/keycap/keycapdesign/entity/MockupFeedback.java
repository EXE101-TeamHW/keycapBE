package com.keycap.keycapdesign.entity;

import com.keycap.keycapdesign.enums.FeedbackType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "mockup_feedbacks")
@EntityListeners(AuditingEntityListener.class)
public class MockupFeedback {

    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mockup_id", nullable = false)
    private Mockup mockup;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private FeedbackType type = FeedbackType.COMMENT;

    @Column(columnDefinition = "nvarchar(max)")
    private String comment;

    @Column(columnDefinition = "nvarchar(max)")
    private String annotationsJson;

    @CreatedDate
    private LocalDateTime createdAt;
}

