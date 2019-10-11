package com.tirsportif.backend.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Positive;

@Data
@Validated
public class CreateLicenseeRequest {

    @Positive
    Integer badgeNumber;

    @Positive
    Integer lockerNumber;

}
