package com.desofs.backend.bootstrapper;

import com.desofs.backend.database.repositories.BookingRepository;
import com.desofs.backend.database.repositories.UserRepository;
import com.desofs.backend.domain.aggregates.BookingDomain;
import com.desofs.backend.domain.valueobjects.Id;
import com.desofs.backend.dtos.IntervalTimeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingBootstrapper {

    private final BookingRepository bookingRepository;

    public void run() throws Exception {
        String userId = "1a1a1a1a-1a1a-1a1a-1a1a-1a1a1a1a1a1a";
        String propertyId = "ccb0198d-1e52-4587-98a6-a5cf8eab0c5c";
        IntervalTimeDto intervalTimeDto = new IntervalTimeDto(new Date(2022, 12, 12), new Date(2022, 12, 15));
        BookingDomain bookingDomain = new BookingDomain(
                intervalTimeDto,
                Id.create(UUID.randomUUID().toString()),
                userId
        );
        bookingRepository.create(bookingDomain, Id.create(propertyId));
    }
}
