package com.desofs.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PaymentDto {

    private String id;
    private float moneyAmount;
    private String creditCardNumber;
    private String cardVerificationCode;
    private LocalDateTime expirationDate;
    private String email;
    private String personalName;
    private LocalDateTime createdAt;

}
