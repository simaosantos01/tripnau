package com.desofs.backend.domain.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoneyAmountTest {

    @Test
    @DisplayName("Test create method with valid money amount")
    void testCreateValidMoneyAmount() {
        MoneyAmount moneyAmount = MoneyAmount.create(100.50f);
        assertNotNull(moneyAmount);
        assertEquals(100.50f, moneyAmount.getValue());
    }

    @Test
    @DisplayName("Test create method with negative money amount")
    void testCreateNegativeMoneyAmount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> MoneyAmount.create(-100.50f));
        assertEquals("MoneyAmount must be positive.", exception.getMessage());
    }
}