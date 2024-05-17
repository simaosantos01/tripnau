package com.desofs.backend.database.springRepositories;

import com.desofs.backend.database.models.BookingDB;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingRepositoryJPA extends CrudRepository<BookingDB, String> {

    List<BookingDB> findByPropertyId(String propertyId);

}
