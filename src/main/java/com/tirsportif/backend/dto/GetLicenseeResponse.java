package com.tirsportif.backend.dto;

import lombok.Value;

import java.time.LocalDate;

@Value
public class GetLicenseeResponse {

    Long id;

    Integer badgeNumber;

    Integer lockerNumber;

    LocalDate subscriptionDate;

    GetShooterResponse shooter;

    GetAddressResponse address;

}
