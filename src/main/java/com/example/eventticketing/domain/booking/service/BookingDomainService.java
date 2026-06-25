package com.example.eventticketing.domain.booking.service;

import com.example.eventticketing.domain.shared.BusinessRuleException;

public class BookingDomainService {

    public void validateCustomerHasNoActiveBooking(boolean hasActiveBookingForSameEvent) {
        if (hasActiveBookingForSameEvent) {
            throw new BusinessRuleException("Customer cannot have more than one active booking for the same event");
        }
    }
}