package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class CreateUserDto {

    @NotNull
    @NotBlank
    private final String name;

    @NotNull
    @NotBlank
    private final String email;

    @NotNull
    @NotBlank
    private final String password;

    @NotNull
    @NotBlank
    private final String role;

    @NotNull
    @NotBlank
    private final boolean isBanned;
}
