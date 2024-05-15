package com.desofs.backend.dtos;

import com.desofs.backend.domain.aggregates.BookingDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class RentalPropertyDto {

    private final String id;
    private final String propertyOwner;
    private final String propertyName;
    private final LocationDto location;
    private final Integer maxGuests;
    private final Integer numBedrooms;
    private final Integer numBathrooms;
    private final String propertyDescription;
    private final BigDecimal amount;
    private final List<PriceNightIntervalDto> priceNightIntervalList;
    private final List<BookingDomain> bookingList;

}
