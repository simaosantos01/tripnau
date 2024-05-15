package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationDto {

    private final double longitude;
    private final double latitude;

}
