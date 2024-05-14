package com.desofs.backend.domain.valueobjects;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class PriceNightInterval {

    private final MoneyAmount price;
    private final IntervalTime interval;

    private PriceNightInterval(MoneyAmount price, IntervalTime interval) {
        this.price = price;
        this.interval = interval;
    }

    public static PriceNightInterval create(MoneyAmount price, IntervalTime interval) {
        notNull(price,
                "Price must not be null.");
        notNull(interval,
                "Interval must not be null.");
        return new PriceNightInterval(price, interval);
    }

    public MoneyAmount getPrice() {
        return price.copy();
    }

    public IntervalTime value() {
        return interval.copy();
    }
}