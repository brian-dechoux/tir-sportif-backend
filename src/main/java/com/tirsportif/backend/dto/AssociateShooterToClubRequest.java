package com.tirsportif.backend.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Validated
public class AssociateShooterToClubRequest {

    @NotNull
    @Positive
    Long clubId;

}
