package com.tirsportif.backend.model.projection;

import com.tirsportif.backend.model.PriceType;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public interface ShooterBillProjection {

    Long getId();

    Double getValue();

    Boolean getPaid();

    PriceType getType();

    String getChallengeName();

    OffsetDateTime getChallengeStartDate();

    LocalDate getLicenseSubscriptionDate();

}
