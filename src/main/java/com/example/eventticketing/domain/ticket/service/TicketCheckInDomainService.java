package com.example.eventticketing.domain.ticket.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.eventticketing.domain.shared.BusinessRuleException;

public class TicketCheckInDomainService {

    public void validateCheckInWindow(LocalDate eventDate, LocalDateTime checkInTime) {
        if (!checkInTime.toLocalDate().equals(eventDate)) {
            throw new BusinessRuleException("Check-in can only be performed on the event day");
        }
    }
}