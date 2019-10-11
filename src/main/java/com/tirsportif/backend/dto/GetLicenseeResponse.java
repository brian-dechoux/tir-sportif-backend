package com.tirsportif.backend.dto;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class GetLicenseeResponse {

    Long id;

    Integer badgeNumber;

    Integer lockerNumber;

    OffsetDateTime subscriptionDate;

    GetShooterResponse shooter;

}
