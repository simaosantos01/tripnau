package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {

    private final String id;
    private final String name;
    private final String email;
    private final String password;
    private final boolean banned;

}
