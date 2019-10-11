package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class AssociateLicenseeToShooterRequest {

    @NotNull
    @Positive
    Long shooterId;

}
