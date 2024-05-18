package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateRentalPropertyDto {
    private final String propertyName;
    private LocationDto location;
    private final int maxGuests;
    private final int numBedrooms;
    private final int numBathrooms;
    private final String propertyDescription;
    private final BigDecimal amount;
    private final List<PriceNightIntervalDto> priceNightIntervalList;
}
