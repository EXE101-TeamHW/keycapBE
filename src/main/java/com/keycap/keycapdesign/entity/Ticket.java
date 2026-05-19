package com.keycap.keycapdesign.entity;

import com.keycap.keycapdesign.enums.TicketStatus;
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

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket extends BaseEntity {

    @Column(name = "ticket_code", unique = true, nullable = false)
    private String ticketCode;

    @OneToOne
    @JoinColumn(name = "request_id", nullable = false)
    private CustomRequest request;

    @ManyToOne
    @JoinColumn(name = "assigned_staff_id")
    private User assignedStaff;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    private LocalDate deadline;

    @Column(name = "revision_count")
    private Integer revisionCount = 0;

    @Column(name = "max_revisions")
    private Integer maxRevisions = 3;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.PENDING;
}

