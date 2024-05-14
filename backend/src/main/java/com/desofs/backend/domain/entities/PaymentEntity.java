package com.desofs.backend.domain.entities;

import com.desofs.backend.domain.valueobjects.*;
import lombok.Getter;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.Validate.notNull;

public class PaymentEntity {

    private Id id;
    private MoneyAmount moneyAmount;
    private CreditCardNumber creditCardNumber;
    private CardVerificationCode cardVerificationCode;
    @Getter
    private LocalDateTime expirationDate;
    private Email email;
    private Name name;
    @Getter
    private LocalDateTime createdAt;

    public PaymentEntity(Id id, MoneyAmount moneyAmount, CreditCardNumber creditCardNumber,
                         CardVerificationCode cardVerificationCode, LocalDateTime expirationDate,
                         Email email, Name personalName, LocalDateTime createdAt) {
        notNull(id, "Id must not be null.");
        notNull(moneyAmount, "Money amount must not be null.");
        notNull(creditCardNumber, "Credit card number must not be null.");
        notNull(cardVerificationCode, "Card verification code must not be null.");
        notNull(expirationDate, "Expiration date must not be null.");
        notNull(email, "Email must not be null.");
        notNull(personalName, "Personal name must not be null.");
        notNull(createdAt, "Creation date must not be null.");

        this.id = id.copy();
        this.moneyAmount = moneyAmount.copy();
        this.creditCardNumber = creditCardNumber.copy();
        this.cardVerificationCode = cardVerificationCode.copy();
        this.expirationDate = expirationDate;
        this.email = email.copy();
        this.name = personalName.copy();
        this.createdAt = createdAt;
    }

    public PaymentEntity(String id, float moneyAmount, String creditCardNumber,
                         String cardVerificationCode, LocalDateTime expirationDate,
                         String email, String name, LocalDateTime createdAt) {
        this(Id.create(id),
                MoneyAmount.create(moneyAmount),
                CreditCardNumber.create(creditCardNumber),
                CardVerificationCode.create(cardVerificationCode),
                expirationDate,
                Email.create(email),
                Name.create(name),
                createdAt);
    }

    public Id getId() {
        return id.copy();
    }

    public MoneyAmount getMoneyAmount() {
        return moneyAmount.copy();
    }

    public CreditCardNumber getCreditCardNumber() {
        return creditCardNumber.copy();
    }

    public CardVerificationCode getCardVerificationCode() {
        return cardVerificationCode.copy();
    }

    public Email getEmail() {
        return email.copy();
    }

    public Name getName() {
        return name.copy();
    }

}
