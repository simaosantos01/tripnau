package com.desofs.backend.database.springRepositories;

import com.desofs.backend.database.models.BookingDB;
import com.desofs.backend.database.models.PaymentDB;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepositoryJPA extends CrudRepository<PaymentDB, String> {

    PaymentDB findByBookingId(String bookingId);

}
