package com.desofs.backend.database.mappers;

import com.desofs.backend.database.models.RentalPropertyDB;
import com.desofs.backend.domain.aggregates.BookingDomain;
import com.desofs.backend.domain.aggregates.RentalPropertyDomain;
import com.desofs.backend.domain.valueobjects.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
public class RentalPropertyMapper {

    public RentalPropertyDomain toDomainObject(RentalPropertyDB rentalProperty,
                                               ArrayList<PriceNightInterval> priceNightIntervals,
                                               ArrayList<BookingDomain> bookings) {
        return new RentalPropertyDomain(Id.create(rentalProperty.getId()),
                Id.create(rentalProperty.getPropertyOwnerId()), PropertyName.create(rentalProperty.getPropertyName()),
                Location.create(rentalProperty.getLat(), rentalProperty.getLon()),
                PositiveInteger.create(rentalProperty.getMaxGuests()),
                PositiveInteger.create(rentalProperty.getNumBedrooms()),
                PositiveInteger.create(rentalProperty.getNumBathrooms()),
                PropertyDescription.create(rentalProperty.getPropertyDescription()),
                MoneyAmount.create(new BigDecimal(rentalProperty.getPriceNightDefault())),
                priceNightIntervals,
                bookings
        );
    }

    public RentalPropertyDB toDatabaseObject(RentalPropertyDomain rentalProperty) {
        return new RentalPropertyDB(rentalProperty.getId().value(), rentalProperty.getAmount().value().toString(),
                rentalProperty.getPropertyOwner().value(), rentalProperty.getPropertyName().value(),
                rentalProperty.getLocation().getLat(), rentalProperty.getLocation().getLon(),
                rentalProperty.getMaxGuests().value(), rentalProperty.getNumBedrooms().value(),
                rentalProperty.getNumBathrooms().value(), rentalProperty.getPropertyDescription().value());
    }
}
