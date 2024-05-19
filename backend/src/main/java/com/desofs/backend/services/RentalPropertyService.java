package com.desofs.backend.services;

import com.desofs.backend.database.mappers.RentalPropertyMapper;
import com.desofs.backend.database.repositories.PriceNightIntervalRepository;
import com.desofs.backend.database.repositories.RentalPropertyRepository;
import com.desofs.backend.domain.aggregates.RentalPropertyDomain;
import com.desofs.backend.domain.valueobjects.PriceNightInterval;
import com.desofs.backend.dtos.CreateRentalPropertyDto;
import com.desofs.backend.dtos.FetchRentalPropertyDto;
import com.desofs.backend.dtos.UpdateRentalPropertyDto;
import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalPropertyService {

    private final RentalPropertyRepository rentalPropertyRepository;

    private final PriceNightIntervalRepository priceNightIntervalRepository;

    private final RentalPropertyMapper rentalPropertyMapper = new RentalPropertyMapper();

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
    public FetchRentalPropertyDto findById(String id) throws NotFoundException {
        RentalPropertyDomain rentalProperty = this.rentalPropertyRepository.findById(id);

        if (rentalProperty == null) {
            throw new NotFoundException("Rental property not found");
        }

        return rentalPropertyMapper.domainToDto(rentalProperty);
    }

    @Transactional
    public List<FetchRentalPropertyDto> findAllByUserId(String id) {
        List<RentalPropertyDomain> rentalPropertyList = this.rentalPropertyRepository.findAllByUserId(id);

        return rentalPropertyList.stream().map(rentalPropertyMapper::domainToDto).toList();
    }

    @Transactional
    public FetchRentalPropertyDto deactivate(String id) throws DatabaseException, NotFoundException {
        RentalPropertyDomain rentalProperty = this.rentalPropertyRepository.findById(id);

        if (rentalProperty == null) {
            throw new NotFoundException("Rental property not found");
        }

        this.rentalPropertyRepository.update(rentalProperty.deactivate());
        return rentalPropertyMapper.domainToDto(rentalProperty);
    }

    @Transactional
    public FetchRentalPropertyDto update(String id, UpdateRentalPropertyDto dto) throws DatabaseException, NotFoundException {
        RentalPropertyDomain rentalProperty = this.rentalPropertyRepository.findById(id);

        if (rentalProperty == null) {
            throw new NotFoundException("Rental property not found");
        }

        RentalPropertyDomain updated =  this.rentalPropertyRepository.update(rentalProperty.update(dto));
        return rentalPropertyMapper.domainToDto(updated);
    }
}
