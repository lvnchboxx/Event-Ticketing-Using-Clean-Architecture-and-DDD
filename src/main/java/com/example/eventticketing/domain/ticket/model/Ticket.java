package com.example.eventticketing.domain.ticket.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.eventticketing.domain.shared.BusinessRuleException;

import lombok.Getter;

@Getter
public class Ticket {

    private final UUID id;
    private final UUID bookingId;
    private final UUID eventId;
    private final String ticketCode;
    private TicketStatus status;
    private LocalDateTime checkedInAt;

    public Ticket(UUID bookingId, UUID eventId) {
        this.id = UUID.randomUUID();
        this.bookingId = bookingId;
        this.eventId = eventId;
        this.ticketCode = UUID.randomUUID().toString();
        this.status = TicketStatus.ACTIVE;
    }

    public void checkIn(UUID eventId, LocalDateTime checkInTime) {
        if (!this.eventId.equals(eventId)) {
            throw new BusinessRuleException("Ticket does not match the event");
        }

        if (status == TicketStatus.CHECKED_IN) {
            throw new BusinessRuleException("Ticket has already been checked in");
        }

        if (status != TicketStatus.ACTIVE) {
            throw new BusinessRuleException("Only active ticket can be checked in");
        }

        this.status = TicketStatus.CHECKED_IN;
        this.checkedInAt = checkInTime;
    }

    public void cancel() {
        if (status == TicketStatus.CHECKED_IN) {
            throw new BusinessRuleException("Checked-in ticket cannot be cancelled");
        }

        this.status = TicketStatus.CANCELLED;
    }
}