package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class CreateUserDto {
    private final String name;
    private final String email;
    private final String password;
    private final String role;
}