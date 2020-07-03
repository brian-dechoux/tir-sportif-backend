package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class CreateLicenseeRequest {

    @Positive
    Integer badgeNumber;

    @Positive
    Integer lockerNumber;

    @NotNull
    @Positive
    Long shooterId;

    @Valid
    CreateAddressRequest address;

}
