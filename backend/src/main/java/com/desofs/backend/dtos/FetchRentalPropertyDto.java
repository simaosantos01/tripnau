package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class FetchRentalPropertyDto {
    private final String id;
    private final String propertyOwner;
    private final String propertyName;
    private final double lat;
    private final double lon;
    private final int maxGuests;
    private final int numBedrooms;
    private final int numBathrooms;
    private final String propertyDescription;
    private final BigDecimal amount;
}