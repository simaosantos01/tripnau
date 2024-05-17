package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreatePaymentDto {
    private final BigDecimal moneyAmount;
    private final String creditCardNumber;
    private final String cardVerificationCode;
    private final LocalDateTime expirationDate;
    private final String email;
    private final String personName;
    private final LocalDateTime createdAt;
}
