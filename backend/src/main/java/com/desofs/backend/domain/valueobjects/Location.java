package com.desofs.backend.domain.valueobjects;

import lombok.Getter;

import static com.desofs.backend.utils.LocationUtils.isValidLatitude;
import static com.desofs.backend.utils.LocationUtils.isValidLongitude;
import static org.apache.commons.lang3.Validate.isTrue;

@Getter
public class Location {

    private final double longitude;
    private final double latitude;

    private Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static Location create(double longitude, double latitude) {
        isTrue(isValidLongitude(longitude), "Invalid longitude value.");
        isTrue(isValidLatitude(latitude), "Invalid latitude value.");
        return new Location(longitude, latitude);
    }


}
