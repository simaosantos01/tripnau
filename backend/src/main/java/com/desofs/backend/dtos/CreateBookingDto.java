package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreateBookingDto {
    private final String propertyId;
    private CreatePaymentDto payment;
    private IntervalTimeDto intervalTime;
}