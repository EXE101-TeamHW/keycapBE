package com.keycap.keycapdesign.dto.ticket;

import com.keycap.keycapdesign.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;

public class TicketStatusUpdateRequest {
    @NotNull
    private TicketStatus status;

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}

