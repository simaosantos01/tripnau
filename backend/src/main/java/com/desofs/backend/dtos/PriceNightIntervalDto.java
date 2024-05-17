package com.desofs.backend.dtos;

import com.desofs.backend.domain.valueobjects.IntervalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PriceNightIntervalDto {

    private final BigDecimal price;
    private final IntervalTimeDto interval;

}
