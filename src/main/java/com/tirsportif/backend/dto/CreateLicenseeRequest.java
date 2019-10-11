package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class CreateLicenseeRequest {

    @Positive
    Integer badgeNumber;

    @Positive
    Integer lockerNumber;

}
