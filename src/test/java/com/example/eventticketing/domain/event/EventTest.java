package com.example.eventticketing.domain.event;

import com.example.eventticketing.domain.event.model.Event;
import com.example.eventticketing.domain.event.model.EventStatus;
import com.example.eventticketing.domain.event.model.TicketCategory;
import com.example.eventticketing.domain.shared.BusinessRuleException;
import com.example.eventticketing.domain.shared.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void eventCannotBeCreatedWithInvalidSchedule() {
        assertThrows(BusinessRuleException.class, () -> new Event(
                UUID.randomUUID(),
                "Concert",
                "Music concert",
                LocalDateTime.of(2026, 6, 10, 10, 0),
                LocalDateTime.of(2026, 6, 9, 10, 0),
                "Jakarta",
                100
        ));
    }

    @Test
    void eventCannotBeCreatedWithZeroCapacity() {
        assertThrows(BusinessRuleException.class, () -> new Event(
                UUID.randomUUID(),
                "Concert",
                "Music concert",
                LocalDateTime.of(2026, 6, 10, 10, 0),
                LocalDateTime.of(2026, 6, 10, 12, 0),
                "Jakarta",
                0
        ));
    }

    @Test
    void newlyCreatedEventMustHaveDraftStatus() {
        Event event = createValidEvent();

        assertEquals(EventStatus.DRAFT, event.getStatus());
    }

    @Test
    void eventCannotBePublishedWithoutActiveTicketCategory() {
        Event event = createValidEvent();

        assertThrows(BusinessRuleException.class, event::publish);
    }

    @Test
    void eventCanBePublishedWithActiveTicketCategory() {
        Event event = createValidEvent();

        TicketCategory category = new TicketCategory(
                "Regular",
                new Money(BigDecimal.valueOf(100000), Currency.getInstance("IDR")),
                50,
                LocalDateTime.of(2026, 5, 1, 10, 0),
                LocalDateTime.of(2026, 6, 9, 10, 0),
                event.getStartDate()
        );

        event.addTicketCategory(category);
        event.publish();

        assertEquals(EventStatus.PUBLISHED, event.getStatus());
    }

    @Test
    void ticketCategoryQuotaCannotExceedEventCapacity() {
        Event event = createValidEvent();

        TicketCategory category = new TicketCategory(
                "Regular",
                new Money(BigDecimal.valueOf(100000), Currency.getInstance("IDR")),
                150,
                LocalDateTime.of(2026, 5, 1, 10, 0),
                LocalDateTime.of(2026, 6, 9, 10, 0),
                event.getStartDate()
        );

        assertThrows(BusinessRuleException.class, () -> event.addTicketCategory(category));
    }

    private Event createValidEvent() {
        return new Event(
                UUID.randomUUID(),
                "Concert",
                "Music concert",
                LocalDateTime.of(2026, 6, 10, 10, 0),
                LocalDateTime.of(2026, 6, 10, 12, 0),
                "Jakarta",
                100
        );
    }
}