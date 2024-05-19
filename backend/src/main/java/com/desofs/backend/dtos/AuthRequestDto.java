package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class AuthRequestDto {

    @NotNull
    @Email
    String email;

    @NotNull
    String password;
}
