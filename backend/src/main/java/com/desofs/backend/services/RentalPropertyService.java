package com.desofs.backend.services;

import com.desofs.backend.database.mappers.RentalPropertyMapper;
import com.desofs.backend.database.repositories.PriceNightIntervalRepository;
import com.desofs.backend.database.repositories.RentalPropertyRepository;
import com.desofs.backend.domain.aggregates.RentalPropertyDomain;
import com.desofs.backend.domain.valueobjects.PriceNightInterval;
import com.desofs.backend.dtos.CreateRentalPropertyDto;
import com.desofs.backend.dtos.FetchRentalPropertyDto;
import com.desofs.backend.exceptions.DatabaseException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("RentalPropertyService")
public class RentalPropertyService {

    private final RentalPropertyRepository rentalPropertyRepository;

    private final PriceNightIntervalRepository priceNightIntervalRepository;

    private final RentalPropertyMapper rentalPropertyMapper = new RentalPropertyMapper();

    public RentalPropertyService(RentalPropertyRepository rentalPropertyRepository,
                                 PriceNightIntervalRepository priceNightIntervalRepository) {
        this.rentalPropertyRepository = rentalPropertyRepository;
        this.priceNightIntervalRepository = priceNightIntervalRepository;
    }

    @Transactional
    public FetchRentalPropertyDto create(CreateRentalPropertyDto createRentalPropertyDto, String userId)
            throws DatabaseException {
        RentalPropertyDomain rentalProperty = new RentalPropertyDomain(createRentalPropertyDto, userId);

        this.rentalPropertyRepository.create(rentalProperty);

        for (PriceNightInterval priceNightInterval : rentalProperty.getPriceNightIntervalList()) {
            this.priceNightIntervalRepository.create(priceNightInterval);
        }


        return rentalPropertyMapper.domainToDto(rentalProperty);
    }

    @Transactional
    public FetchRentalPropertyDto findById(String id) {
        RentalPropertyDomain rentalProperty = this.rentalPropertyRepository.findById(id);

        return rentalPropertyMapper.domainToDto(rentalProperty);
    }
}
