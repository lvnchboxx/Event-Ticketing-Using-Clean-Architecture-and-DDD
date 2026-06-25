package com.example.eventticketing.domain.booking.repository;

import java.util.Optional;
import java.util.UUID;

import com.example.eventticketing.domain.booking.model.Booking;

public interface BookingRepository {

    Booking save(Booking booking);

    Optional<Booking> findById(UUID id);

    boolean existsActiveBookingByCustomerIdAndEventId(UUID customerId, UUID eventId);
}