package com.desofs.backend.database.repositories;

import com.desofs.backend.database.mappers.BookingMapper;
import com.desofs.backend.database.models.BookingDB;
import com.desofs.backend.database.springRepositories.BookingRepositoryJPA;
import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.database.mappers.RentalPropertyMapper;
import com.desofs.backend.database.models.RentalPropertyDB;
import com.desofs.backend.database.springRepositories.RentalPropertyRepositoryJPA;
import com.desofs.backend.domain.aggregates.BookingDomain;
import com.desofs.backend.domain.aggregates.RentalPropertyDomain;
import com.desofs.backend.domain.valueobjects.PriceNightInterval;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("RentalPropertyRepositoryCapsule")
@RequiredArgsConstructor
public class RentalPropertyRepository {

    private final RentalPropertyRepositoryJPA rentalPropertyRepository;

    private final BookingRepositoryJPA bookingRepository;

    private final RentalPropertyMapper propertyMapper;

    private final BookingMapper bookingMapper;

    public void create(RentalPropertyDomain rentalProperty) throws DatabaseException {
        Optional<RentalPropertyDB> rentalPropertyById =
                this.rentalPropertyRepository.findById(rentalProperty.getId().value());

        if (rentalPropertyById.isPresent()) {
            throw new DatabaseException("Duplicated ID violation.");
        }

        this.rentalPropertyRepository.save(this.propertyMapper.toDatabaseObject(rentalProperty));
    }

    public RentalPropertyDomain findById(String id) {
        try {
            Optional<RentalPropertyDB> rentalProperty = this.rentalPropertyRepository.findById(id);
            return rentalProperty.map(rp -> this.propertyMapper.toDomainObject(rp, joinPriceNightIntervals(rp), joinBookings(rp))).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private List<PriceNightInterval> joinPriceNightIntervals(RentalPropertyDB rp) {
        // todo: implement
        return new ArrayList<>();
    }

    private List<BookingDomain> joinBookings(RentalPropertyDB rp) {
        // todo: implement
        return new ArrayList<>();
    }
}
