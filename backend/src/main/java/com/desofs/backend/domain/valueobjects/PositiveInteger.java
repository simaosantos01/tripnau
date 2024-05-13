package com.desofs.backend.domain.valueobjects;

import static org.apache.commons.lang3.Validate.isTrue;

public class PositiveInteger {

    private final int value;

    public PositiveInteger(int value) {
        isTrue(value >= 0, "Value must be a positive integer.");
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
