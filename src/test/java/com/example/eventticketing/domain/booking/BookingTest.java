package com.example.eventticketing.domain.booking;

import com.example.eventticketing.domain.booking.model.Booking;
import com.example.eventticketing.domain.booking.model.BookingStatus;
import com.example.eventticketing.domain.shared.BusinessRuleException;
import com.example.eventticketing.domain.shared.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void bookingCannotBeCreatedWithZeroQuantity() {
        assertThrows(BusinessRuleException.class, () -> new Booking(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                0,
                new Money(BigDecimal.valueOf(100000), Currency.getInstance("IDR")),
                LocalDateTime.now()
        ));
    }

    @Test
    void newBookingMustHavePendingPaymentStatus() {
        Booking booking = createValidBooking();

        assertEquals(BookingStatus.PENDING_PAYMENT, booking.getStatus());
    }

    @Test
    void bookingCannotBePaidAfterPaymentDeadline() {
        Booking booking = createValidBooking();

        assertThrows(BusinessRuleException.class, () -> booking.pay(
                new Money(BigDecimal.valueOf(100000), Currency.getInstance("IDR")),
                booking.getPaymentDeadline().plusMinutes(1)
        ));
    }

    @Test
    void bookingCannotBePaidWithIncorrectAmount() {
        Booking booking = createValidBooking();

        assertThrows(BusinessRuleException.class, () -> booking.pay(
                new Money(BigDecimal.valueOf(50000), Currency.getInstance("IDR")),
                LocalDateTime.now()
        ));
    }

    @Test
    void paidBookingCannotExpire() {
        Booking booking = createValidBooking();

        booking.pay(
                new Money(BigDecimal.valueOf(100000), Currency.getInstance("IDR")),
                LocalDateTime.now()
        );

        assertThrows(BusinessRuleException.class, () -> booking.expire(
                booking.getPaymentDeadline().plusMinutes(1)
        ));
    }

    private Booking createValidBooking() {
        return new Booking(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                1,
                new Money(BigDecimal.valueOf(100000), Currency.getInstance("IDR")),
                LocalDateTime.now()
        );
    }
}