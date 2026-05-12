package com.example.eventticketing.domain.shared;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {

    @Test
    void moneyCannotBeNegative() {
        assertThrows(BusinessRuleException.class, () -> new Money(
                BigDecimal.valueOf(-1000),
                Currency.getInstance("IDR")
        ));
    }

    @Test
    void moneyCurrencyCannotBeNull() {
        assertThrows(BusinessRuleException.class, () -> new Money(
                BigDecimal.valueOf(1000),
                null
        ));
    }
}