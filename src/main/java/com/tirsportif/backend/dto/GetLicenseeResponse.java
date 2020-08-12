package com.tirsportif.backend.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
public class GetLicenseeResponse {

    @NotNull
    Long id;

    @NotNull
    Integer badgeNumber;

    Integer lockerNumber;

    @NotNull
    LocalDate subscriptionDate;

    @NotNull
    GetShooterResponse shooter;

    @NotNull
    GetAddressResponse address;

}
