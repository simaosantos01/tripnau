package com.desofs.backend.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {
/*     @Test
    public void testValidLocationCreation() {
        double longitude = 10.0;
        double latitude = 20.0;
        Location location = Location.create(longitude, latitude);

        assertTrue(location.getLon() == longitude);
        assertTrue(location.getLat() == latitude);
    } */

    @Test
    public void testInvalidLongitude() {
        double invalidLongitude = -190.0;
        double validLatitude = 20.0;
        assertThrows(IllegalArgumentException.class, () -> Location.create(invalidLongitude, validLatitude));
    }

/*     @Test
    public void testInvalidLatitude() {
        double validLongitude = 10.0;
        double invalidLatitude = -100.0;
        assertThrows(IllegalArgumentException.class, () -> Location.create(validLongitude, invalidLatitude));
    } */

    /* @Test
    public void testValidLongitudeBoundary() {
        double minLongitude = -180.0;
        double maxLongitude = 180.0;
        double validLatitude = 20.0;

        Location minLocation = Location.create(minLongitude, validLatitude);
        Location maxLocation = Location.create(maxLongitude, validLatitude);

        assertTrue(minLocation.getLon() == minLongitude);
        assertTrue(maxLocation.getLon() == maxLongitude);
    } */

/*     @Test
    public void testValidLatitudeBoundary() {
        double minLatitude = -90.0;
        double maxLatitude = 90.0;
        double validLongitude = 10.0;

        Location minLocation = Location.create(validLongitude, minLatitude);
        Location maxLocation = Location.create(validLongitude, maxLatitude);

        assertTrue(minLocation.getLat() == minLatitude);
        assertTrue(maxLocation.getLat() == maxLatitude);
    } */
}