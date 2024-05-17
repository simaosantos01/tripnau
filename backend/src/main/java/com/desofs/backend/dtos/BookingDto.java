package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class BookingDto {

    private final String id;
    private final String accountId;
    private PaymentDto payment;
    private IntervalTimeDto intervalTime;
    private List<EventDto> eventList;
    private ReviewDto review;
    private LocalDateTime createdAt;
}