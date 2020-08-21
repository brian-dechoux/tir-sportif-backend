package com.tirsportif.backend.dto;

import com.tirsportif.backend.model.PriceType;
import lombok.Value;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Value
public class GetShooterBillResponse {

    long id;

    double value;

    boolean paid;

    OffsetDateTime paidDate;

    PriceType priceType;

    String challengeName;

    OffsetDateTime startDate;

    LocalDate subscriptionDate;

}
