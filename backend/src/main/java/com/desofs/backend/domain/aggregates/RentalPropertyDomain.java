package com.desofs.backend.domain.aggregates;

import com.desofs.backend.domain.valueobjects.*;
import com.desofs.backend.dtos.CreateRentalPropertyDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.notNull;

public class RentalPropertyDomain {
    private final Id id;
    private final Id propertyOwner;
    private final PropertyName propertyName;
    private final Location location;
    private final PositiveInteger maxGuests;
    private final PositiveInteger numBedrooms;
    private final PositiveInteger numBathrooms;
    private final PropertyDescription propertyDescription;
    private final MoneyAmount amount;
    private final List<PriceNightInterval> priceNightIntervalList;
    private List<BookingDomain> bookingList;
    private final boolean isActive;

    // Constructors ----------------------------------------------------------------------------------------------------

    public RentalPropertyDomain(Id id, Id propertyOwner, PropertyName propertyName, Location location,
                                PositiveInteger maxGuests, PositiveInteger numBedrooms, PositiveInteger numBathrooms,
                                PropertyDescription propertyDescription, MoneyAmount amount,
                                List<PriceNightInterval> priceNightIntervalList, List<BookingDomain> bookingList,
                                boolean isActive) {
        notNull(id, "Id must not be null.");
        notNull(propertyOwner, "PropertyOwner must not be null.");
        notNull(propertyName, "PropertyName must not be null.");
        notNull(location, "Location must not be null.");
        notNull(maxGuests, "MaxGuests must not be null.");
        notNull(numBedrooms, "NumBedrooms must not be null.");
        notNull(numBathrooms, "NumBathrooms must not be null.");
        notNull(propertyDescription, "PropertyDescription must not be null.");
        notNull(amount, "Amount must not be null.");
        notNull(priceNightIntervalList, "PriceNightIntervalList must not be null.");
        notNull(bookingList, "BookingList must not be null.");

        this.id = id.copy();
        this.propertyOwner = propertyOwner.copy();
        this.propertyName = propertyName.copy();
        this.location = location.copy();
        this.maxGuests = maxGuests.copy();
        this.numBedrooms = numBedrooms.copy();
        this.numBathrooms = numBathrooms.copy();
        this.propertyDescription = propertyDescription.copy();
        this.amount = amount.copy();
        this.priceNightIntervalList = priceNightIntervalList;
        this.bookingList = bookingList;
        this.isActive = isActive;
    }

    // Used to create a rental property
    public RentalPropertyDomain(CreateRentalPropertyDto dto, String userId) {
        notNull(userId, "PropertyOwner must not be null.");
        notNull(dto.getPropertyName(), "PropertyName must not be null.");
        notNull(dto.getLocation(), "Location must not be null.");
        notNull(dto.getPropertyDescription(), "PropertyDescription must not be null.");
        notNull(dto.getAmount(), "Amount must not be null.");
        notNull(dto.getPriceNightIntervalList(), "PriceNightIntervalList must not be null.");

        this.id = Id.create(UUID.randomUUID().toString());
        this.propertyOwner = Id.create(userId);
        this.propertyName = PropertyName.create(dto.getPropertyName());
        this.location = Location.create(dto.getLocation().getLat(), dto.getLocation().getLon());
        this.maxGuests = PositiveInteger.create(dto.getMaxGuests());
        this.numBedrooms = PositiveInteger.create(dto.getNumBedrooms());
        this.numBathrooms = PositiveInteger.create(dto.getNumBathrooms());
        this.propertyDescription = PropertyDescription.create(dto.getPropertyDescription());
        this.amount = MoneyAmount.create(dto.getAmount());
        this.priceNightIntervalList = getPriceNightIntervalsList(id, dto);
        this.bookingList = new ArrayList<>();
        this.isActive = true;
    }

    private static List<PriceNightInterval> getPriceNightIntervalsList(Id id, CreateRentalPropertyDto dto) {
        return dto.getPriceNightIntervalList().stream().map(value -> {
            MoneyAmount tempMoneyAmount = MoneyAmount.create(value.getPrice());
            IntervalTime tempIntervalTime = IntervalTime.create(value.getInterval().getFrom(), value.getInterval().getTo());
            return new PriceNightInterval(id, tempMoneyAmount, tempIntervalTime);
        }).toList();
    }

    // Getters ---------------------------------------------------------------------------------------------------------

    public Id getId() {
        return id.copy();
    }

    public Id getPropertyOwner() {
        return propertyOwner.copy();
    }

    public PropertyName getPropertyName() {
        return propertyName.copy();
    }

    public Location getLocation() {
        return location.copy();
    }

    public PositiveInteger getMaxGuests() {
        return maxGuests.copy();
    }

    public PositiveInteger getNumBedrooms() {
        return numBedrooms.copy();
    }

    public PositiveInteger getNumBathrooms() {
        return numBathrooms.copy();
    }

    public PropertyDescription getPropertyDescription() {
        return propertyDescription.copy();
    }

    public MoneyAmount getAmount() {
        return amount.copy();
    }

    public List<PriceNightInterval> getPriceNightIntervalList() {
        return List.copyOf(priceNightIntervalList);
    }

    public List<BookingDomain> getBookingList() {
        return List.copyOf(bookingList);
    }

    public boolean getIsActive() {
        return isActive;
    }

    // Domain methods --------------------------------------------------------------------------------------------------

    private boolean bookingAlreadyExists(BookingDomain bookingDomain) {
        return !this.bookingList.stream().filter(b -> b.getId().value().equals(bookingDomain.getId().value()))
                .toList()
                .isEmpty();
    }

    private boolean timeIntervalIntercepts(PriceNightInterval priceNightInterval) {
        return !this.priceNightIntervalList.stream().allMatch(existingInterval -> {
            IntervalTime a = existingInterval.getInterval();
            IntervalTime b = priceNightInterval.getInterval();
            return (a.getFrom().before(b.getFrom()) && a.getTo().before(b.getFrom())) || // precedes
                    (a.getTo().equals(b.getFrom())) || // meets
                    (b.getTo().equals(a.getFrom())) || // met by
                    (b.getFrom().before(a.getFrom()) && b.getTo().before(a.getFrom())); // preceded by
        });
    }

    public void addBooking(BookingDomain bookingDomain) {
        if (bookingAlreadyExists(bookingDomain)) {
            throw new IllegalArgumentException("There is already a booking with that id associated.");
        }

        // todo: validar se o booking tem a info que encaixa na property (intervals, etc)
        this.bookingList.add(bookingDomain);
    }

    public Double getAverageStars() {
        return this.bookingList.stream().mapToDouble(b -> b.getReview().getStars().value()).average().orElse(0.0);
    }

    public void addPriceNightInterval(PriceNightInterval priceNightInterval) {
        if (timeIntervalIntercepts(priceNightInterval)) {
            throw new IllegalArgumentException("The time interval intercepts with existing intervals");
        }
        this.priceNightIntervalList.add(priceNightInterval);
    }

}
