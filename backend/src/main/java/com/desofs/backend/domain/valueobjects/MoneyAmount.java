package com.desofs.backend.domain.valueobjects;

import lombok.Getter;

import static org.apache.commons.lang3.Validate.isTrue;

@Getter
public class MoneyAmount {

    private final float value;

    private MoneyAmount(float value) {
        this.value = value;
    }

    public MoneyAmount create(float value) {
        isTrue(value < 0, "MoneyAmount must be positive.");
        return new MoneyAmount(value);
    }

}
