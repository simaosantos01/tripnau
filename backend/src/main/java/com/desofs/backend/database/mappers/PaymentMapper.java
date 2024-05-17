package com.desofs.backend.database.mappers;

import com.desofs.backend.domain.entities.PaymentEntity;
import com.desofs.backend.domain.valueobjects.*;
import com.desofs.backend.dtos.PaymentDto;

public class PaymentMapper {

    public PaymentDto domainToDto(PaymentEntity payment) {
        return new PaymentDto(
                payment.getId().value(),
                payment.getMoneyAmount().value(),
                payment.getCreditCardNumber().value(),
                payment.getCardVerificationCode().value(),
                payment.getExpirationDate(),
                payment.getEmail().value(),
                payment.getName().value(),
                payment.getCreatedAt());
    }
}