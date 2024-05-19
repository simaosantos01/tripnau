package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PriceNightIntervalDto {
    private final String rentalPropertyId;
    private final BigDecimal price;
    private final IntervalTimeDto interval;
}
