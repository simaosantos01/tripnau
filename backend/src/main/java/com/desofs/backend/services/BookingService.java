package com.desofs.backend.services;

import com.desofs.backend.database.mappers.BookingMapper;
import com.desofs.backend.database.mappers.PaymentMapper;
import com.desofs.backend.database.mappers.RentalPropertyMapper;
import com.desofs.backend.database.repositories.BookingRepository;
import com.desofs.backend.database.repositories.PaymentRepository;
import com.desofs.backend.database.repositories.RentalPropertyRepository;
import com.desofs.backend.database.repositories.UserRepository;
import com.desofs.backend.domain.aggregates.BookingDomain;
import com.desofs.backend.domain.aggregates.RentalPropertyDomain;
import com.desofs.backend.domain.aggregates.UserDomain;
import com.desofs.backend.domain.entities.PaymentEntity;
import com.desofs.backend.domain.valueobjects.Id;
import com.desofs.backend.dtos.*;
import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingService {

    private final RentalPropertyRepository rentalPropertyRepository;

    private final PaymentRepository paymentRepository;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final PaymentMapper paymentMapper;


    @Transactional
    public FetchBookingDto create(CreateBookingDto bookingDto) throws DatabaseException, NotFoundException {
        RentalPropertyDomain rentalProperty = this.rentalPropertyRepository.findById(bookingDto.getPropertyId());
        if (rentalProperty == null) {
            throw new NotFoundException("Rental property not found");
        }

        Id bookingId = Id.create(UUID.randomUUID().toString());
        BookingDomain bookingDomain = new BookingDomain(bookingDto, bookingId);
        rentalProperty.addBooking(bookingDomain);

        bookingRepository.create(bookingDomain, rentalProperty.getId());
        paymentRepository.create(paymentMapper.dtoToDomain(bookingDto.getPayment(), bookingId.value()));

        return this.bookingMapper.domainToDto(bookingDomain);
    }

    @Transactional
    public FetchBookingDto findById(String propertyId, String bookingId) throws NotFoundException {
        RentalPropertyDomain rentalProperty = this.rentalPropertyRepository.findById(propertyId);
        if (rentalProperty == null) {
            throw new NotFoundException("Rental property not found");
        }

        BookingDomain bookingDomain = this.bookingRepository.findById(bookingId);
        if (bookingDomain == null) {
            throw new NotFoundException("Booking not found");
        }

        return bookingMapper.domainToDto(bookingDomain);
    }
}
