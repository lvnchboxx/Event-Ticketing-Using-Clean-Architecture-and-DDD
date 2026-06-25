package com.example.eventticketing.domain.refund.service;

import com.example.eventticketing.domain.shared.BusinessRuleException;

public class RefundDomainService {

    public void validateNoTicketCheckedIn(boolean hasCheckedInTicket) {
        if (hasCheckedInTicket) {
            throw new BusinessRuleException("Refund cannot be requested if a ticket has already been checked in");
        }
    }
}