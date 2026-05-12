package com.example.eventticketing.domain.refund.model;

import java.util.UUID;

import com.example.eventticketing.domain.shared.BusinessRuleException;
import com.example.eventticketing.domain.shared.Money;

public class Refund {

    private final UUID id;
    private final UUID bookingId;
    private final Money amount;
    private RefundStatus status;
    private String rejectionReason;
    private String paymentReference;

    public Refund(UUID bookingId, Money amount) {
        this.id = UUID.randomUUID();
        this.bookingId = bookingId;
        this.amount = amount;
        this.status = RefundStatus.REQUESTED;
    }

    public void approve() {
        if (status != RefundStatus.REQUESTED) {
            throw new BusinessRuleException("Refund can only be approved if status is Requested");
        }

        this.status = RefundStatus.APPROVED;
    }

    public void reject(String reason) {
        if (status != RefundStatus.REQUESTED) {
            throw new BusinessRuleException("Refund can only be rejected if status is Requested");
        }

        if (reason == null || reason.isBlank()) {
            throw new BusinessRuleException("Rejection reason must be provided");
        }

        this.rejectionReason = reason;
        this.status = RefundStatus.REJECTED;
    }

    public void markPaidOut(String paymentReference) {
        if (status != RefundStatus.APPROVED) {
            throw new BusinessRuleException("Only approved refund can be paid out");
        }

        if (paymentReference == null || paymentReference.isBlank()) {
            throw new BusinessRuleException("Payment reference must be recorded");
        }

        this.paymentReference = paymentReference;
        this.status = RefundStatus.PAID_OUT;
    }

    public UUID getId() {
        return id;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public Money getAmount() {
        return amount;
    }

    public RefundStatus getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public String getPaymentReference() {
        return paymentReference;
    }
}