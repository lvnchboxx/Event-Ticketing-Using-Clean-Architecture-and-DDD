package com.example.eventticketing.domain.ticket;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.example.eventticketing.domain.shared.BusinessRuleException;
import com.example.eventticketing.domain.ticket.model.Ticket;
import com.example.eventticketing.domain.ticket.model.TicketStatus;

class TicketTest {

    @Test
    void activeTicketCanBeCheckedIn() {
        UUID eventId = UUID.randomUUID();
        Ticket ticket = new Ticket(UUID.randomUUID(), eventId);

        ticket.checkIn(eventId, LocalDateTime.now());

        assertEquals(TicketStatus.CHECKED_IN, ticket.getStatus());
    }

    @Test
    void checkedInTicketCannotBeCheckedInAgain() {
        UUID eventId = UUID.randomUUID();
        Ticket ticket = new Ticket(UUID.randomUUID(), eventId);

        ticket.checkIn(eventId, LocalDateTime.now());

        assertThrows(BusinessRuleException.class, () -> ticket.checkIn(eventId, LocalDateTime.now()));
    }

    @Test
    void ticketCannotBeCheckedInForDifferentEvent() {
        Ticket ticket = new Ticket(UUID.randomUUID(), UUID.randomUUID());

        assertThrows(BusinessRuleException.class, () -> ticket.checkIn(
                UUID.randomUUID(),
                LocalDateTime.now()
        ));
    }
}