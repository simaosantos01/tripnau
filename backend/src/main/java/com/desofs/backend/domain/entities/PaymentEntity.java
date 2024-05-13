package com.desofs.backend.domain.entities;

import com.desofs.backend.domain.valueobjects.*;

import java.time.LocalDateTime;

public class PaymentEntity {

    private Id id;

    private MoneyAmount moneyAmount;

    private CreditCardNumber creditCardNumber;

    private CardVerificationCode cardVerificationCode;

    private LocalDateTime expirationDate;

    private Email email;

    private PersonalName personalName;

    private LocalDateTime createdAt;

}
