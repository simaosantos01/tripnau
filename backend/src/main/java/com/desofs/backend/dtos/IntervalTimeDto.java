package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class IntervalTimeDto {

    private final Date from;
    private final Date to;

}
