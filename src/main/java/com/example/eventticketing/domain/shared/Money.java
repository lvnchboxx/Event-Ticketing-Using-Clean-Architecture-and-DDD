package com.example.eventticketing.domain.shared;

import java.math.BigDecimal;
import java.util.Currency;

import lombok.Getter;

@Getter
public class Money {

    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new BusinessRuleException("Money amount cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("Money amount cannot be negative");
        }

        if (currency == null) {
            throw new BusinessRuleException("Currency cannot be null");
        }

        this.amount = amount;
        this.currency = currency;
    }

    public Money multiply(int quantity) {
        if (quantity <= 0) {
            throw new BusinessRuleException("Quantity must be greater than zero");
        }

        return new Money(amount.multiply(BigDecimal.valueOf(quantity)), currency);
    }
}