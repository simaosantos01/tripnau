package com.desofs.backend.services;

import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.database.repositories.RentalPropertyRepository;
import com.desofs.backend.domain.aggregates.RentalPropertyDomain;
import com.desofs.backend.domain.valueobjects.*;
import com.desofs.backend.dtos.CreateRentalPropertyDto;
import com.desofs.backend.dtos.FetchRentalPropertyDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component("RentalPropertyService")
public class RentalPropertyService {

    private final RentalPropertyRepository rentalPropertyRepository;

    public RentalPropertyService(RentalPropertyRepository rentalPropertyRepository) {
        this.rentalPropertyRepository = rentalPropertyRepository;
    }

    @Transactional
    public FetchRentalPropertyDto create(CreateRentalPropertyDto createRentalPropertyDto, String userId)
            throws DatabaseException {

        RentalPropertyDomain rentalProperty =
                new RentalPropertyDomain(Id.create(userId),
                        PropertyName.create(createRentalPropertyDto.getPropertyName()),
                        Location.create(createRentalPropertyDto.getLat(), createRentalPropertyDto.getLon()),
                        PositiveInteger.create(createRentalPropertyDto.getMaxGuests()),
                        PositiveInteger.create(createRentalPropertyDto.getNumBedrooms()),
                        PositiveInteger.create(createRentalPropertyDto.getNumBathrooms()),
                        PropertyDescription.create(createRentalPropertyDto.getPropertyDescription()),
                        MoneyAmount.create(createRentalPropertyDto.getAmount()), new ArrayList<>(), new ArrayList<>());

        this.rentalPropertyRepository.create(rentalProperty);

        return new FetchRentalPropertyDto(rentalProperty.getId().value(), rentalProperty.getPropertyOwner().value(),
                rentalProperty.getPropertyName().value(), rentalProperty.getLocation().getLat(),
                rentalProperty.getLocation().getLon(), rentalProperty.getMaxGuests().value(),
                rentalProperty.getNumBedrooms().value(), rentalProperty.getNumBathrooms().value(),
                rentalProperty.getPropertyDescription().value(), rentalProperty.getAmount().value());
    }

    @Transactional
    public FetchRentalPropertyDto findById(String id) {
        RentalPropertyDomain rentalProperty = this.rentalPropertyRepository.findById(id);
        return new FetchRentalPropertyDto(rentalProperty.getId().value(), rentalProperty.getPropertyOwner().value(),
                rentalProperty.getPropertyName().value(), rentalProperty.getLocation().getLat(),
                rentalProperty.getLocation().getLon(), rentalProperty.getMaxGuests().value(),
                rentalProperty.getNumBedrooms().value(), rentalProperty.getNumBathrooms().value(),
                rentalProperty.getPropertyDescription().value(), rentalProperty.getAmount().value());
    }
}
