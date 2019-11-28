package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class AddShotResultRequest {

    @NotNull
    @Positive
    int serieNumber;

    @Positive
    Integer shotNumber;

    @NotNull
    @Positive
    double points;

}
