package com.desofs.backend.domain.valueobjects;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class PriceNightInterval {

    private final MoneyAmount price;
    private final IntervalTime interval;

    public PriceNightInterval(MoneyAmount price, IntervalTime interval) {
        notNull(price, "Price must not be null.");
        notNull(interval, "Interval must not be null.");
        isTrue(price.getValue() >= 0, "Price must be non-negative.");
        this.price = price;
        this.interval = interval;
    }

    public MoneyAmount getPrice() {
        return price;
    }

    public IntervalTime getInterval() {
        return interval;
    }
}