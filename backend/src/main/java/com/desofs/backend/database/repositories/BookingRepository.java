package com.desofs.backend.database.repositories;

import com.desofs.backend.database.mappers.BookingMapper;
import com.desofs.backend.database.mappers.PaymentMapper;
import com.desofs.backend.database.models.BookingDB;
import com.desofs.backend.database.models.ImageUrlDB;
import com.desofs.backend.database.models.PaymentDB;
import com.desofs.backend.database.models.ReviewDB;
import com.desofs.backend.database.springRepositories.BookingRepositoryJPA;
import com.desofs.backend.database.springRepositories.ImageRepositoryJPA;
import com.desofs.backend.database.springRepositories.PaymentRepositoryJPA;
import com.desofs.backend.database.springRepositories.ReviewRepositoryJPA;
import com.desofs.backend.domain.aggregates.BookingDomain;
import com.desofs.backend.domain.valueobjects.Id;
import com.desofs.backend.exceptions.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingRepository {

    private final BookingRepositoryJPA bookingRepository;
    private final PaymentRepositoryJPA paymentRepositoryJpa;
    private final ReviewRepositoryJPA reviewRepositoryJpa;
    private final ImageRepositoryJPA imageRepositoryJpa;
    private final BookingMapper bookingMapper;

    public void create(BookingDomain bookingDomain, Id propertyId) throws DatabaseException {
        BookingDB bookingDB = this.bookingRepository.findById(bookingDomain.getId().value()).orElse(null);

        if (bookingDB != null) {
            throw new DatabaseException("Duplicated ID violation.");
        }

        this.bookingRepository.save(this.bookingMapper.domainToDb(bookingDomain, propertyId.value()));
    }

    public BookingDomain findById(String bookingId) {
        try {
            BookingDB bookingDB = this.bookingRepository.findById(bookingId).orElse(null);

            if (bookingDB != null) {
                PaymentDB paymentDB = this.paymentRepositoryJpa.findByBookingId(bookingId);

                if (paymentDB != null) {
                    ReviewDB reviewDB = this.reviewRepositoryJpa.findByBookingId(bookingId);

                    if (reviewDB != null) {
                        List<ImageUrlDB> imageUrlDB = this.imageRepositoryJpa.findByReviewId(reviewDB.getId());

                        return this.bookingMapper.dbToDomain(bookingDB, paymentDB, reviewDB, imageUrlDB);
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
