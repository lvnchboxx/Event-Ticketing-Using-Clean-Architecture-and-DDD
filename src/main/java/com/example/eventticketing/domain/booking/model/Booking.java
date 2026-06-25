package com.example.eventticketing.domain.booking.model;

import com.example.eventticketing.domain.booking.event.BookingExpired;
import com.example.eventticketing.domain.booking.event.BookingPaid;
import com.example.eventticketing.domain.booking.event.TicketReserved;
import com.example.eventticketing.domain.shared.BusinessRuleException;
import com.example.eventticketing.domain.shared.DomainEvent;
import com.example.eventticketing.domain.shared.Money;
import com.example.eventticketing.domain.ticket.model.Ticket;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Booking {

    private final UUID id;
    private final UUID customerId;
    private final UUID eventId;
    private final UUID ticketCategoryId;
    private final int quantity;
    private final Money totalPrice;
    private final LocalDateTime paymentDeadline;
    private BookingStatus status;

    private final List<Ticket> tickets = new ArrayList<>();
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Booking(
            UUID customerId,
            UUID eventId,
            UUID ticketCategoryId,
            int quantity,
            Money totalPrice,
            LocalDateTime createdAt
    ) {
        if (quantity <= 0) {
            throw new BusinessRuleException("Ticket quantity must be greater than zero");
        }

        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.eventId = eventId;
        this.ticketCategoryId = ticketCategoryId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.paymentDeadline = createdAt.plusMinutes(15);
        this.status = BookingStatus.PENDING_PAYMENT;

        domainEvents.add(new TicketReserved(this.id, eventId, customerId, LocalDateTime.now()));
    }

    public void pay(Money paymentAmount, LocalDateTime paidAt) {
        if (status != BookingStatus.PENDING_PAYMENT) {
            throw new BusinessRuleException("Only pending payment booking can be paid");
        }

        if (paidAt.isAfter(paymentDeadline)) {
            throw new BusinessRuleException("Booking cannot be paid after payment deadline");
        }

        if (!totalPrice.equals(paymentAmount)) {
            throw new BusinessRuleException("Payment amount must equal total booking price");
        }

        this.status = BookingStatus.PAID;

        for (int i = 0; i < quantity; i++) {
            tickets.add(new Ticket(id, eventId));
        }

        domainEvents.add(new BookingPaid(this.id, LocalDateTime.now()));
    }

    public void expire(LocalDateTime now) {
        if (status == BookingStatus.PAID) {
            throw new BusinessRuleException("Paid booking cannot expire");
        }

        if (status == BookingStatus.PENDING_PAYMENT && now.isAfter(paymentDeadline)) {
            this.status = BookingStatus.EXPIRED;
            domainEvents.add(new BookingExpired(this.id, LocalDateTime.now()));
        }
    }

    public void markRefunded() {
        if (status != BookingStatus.PAID) {
            throw new BusinessRuleException("Only paid booking can be refunded");
        }

        this.status = BookingStatus.REFUNDED;
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}