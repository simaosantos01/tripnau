package com.desofs.backend.database.mappers;

import com.desofs.backend.domain.aggregates.BookingDomain;
import com.desofs.backend.dtos.BookingDto;
import com.desofs.backend.dtos.EventDto;
import com.desofs.backend.dtos.IntervalTimeDto;

public class BookingMapper {

    private final PaymentMapper paymentMapper = new PaymentMapper();
    private final ReviewMapper reviewMapper = new ReviewMapper();

    public BookingDto domainToDto(BookingDomain booking) {
        return new BookingDto(
                booking.getId().value(),
                booking.getAccountId().value(),
                paymentMapper.domainToDto(booking.getPayment()),
                new IntervalTimeDto(booking.getIntervalTime().getFrom(), booking.getIntervalTime().getTo()),
                booking.getEventList().stream().map(event -> {
                    return new EventDto(event.getDatetime(), event.getState());
                }).toList(),
                reviewMapper.domainToDto(booking.getReview()),
                booking.getCreatedAt());
    }
}
