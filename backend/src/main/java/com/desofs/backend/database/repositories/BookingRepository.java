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

    private final BookingRepositoryJPA bookingRepositoryJPA;

    private final PaymentRepositoryJPA paymentRepositoryJPA;

    private final ReviewRepositoryJPA reviewRepositoryJPA;

    private final ImageRepositoryJPA imageRepositoryJPA;

    private final BookingMapper bookingMapper;

    public void create(BookingDomain bookingDomain, Id propertyId) throws DatabaseException {
        BookingDB bookingDB = this.bookingRepositoryJPA.findById(bookingDomain.getId().value()).orElse(null);

        if (bookingDB != null) {
            throw new DatabaseException("Duplicated ID violation.");
        }

        this.bookingRepositoryJPA.save(this.bookingMapper.domainToDb(bookingDomain, propertyId.value()));
    }

    public BookingDomain findById(String bookingId) {
        try {
            BookingDB bookingDB = this.bookingRepositoryJPA.findById(bookingId).orElse(null);

            if (bookingDB != null) {
                PaymentDB paymentDB = this.paymentRepositoryJPA.findByBookingId(bookingId).orElse(null);

                if (paymentDB != null) {
                    ReviewDB reviewDB = this.reviewRepositoryJPA.findByBookingId(bookingId);

                    if (reviewDB != null) {
                        List<ImageUrlDB> imagesUrlsDB = this.imageRepositoryJPA.findByReviewId(reviewDB.getId());
                        return this.bookingMapper.dbToDomain(bookingDB, paymentDB, reviewDB, imagesUrlsDB);
                    } else {
                        return this.bookingMapper.dbToDomain(bookingDB, paymentDB, null, null);
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
