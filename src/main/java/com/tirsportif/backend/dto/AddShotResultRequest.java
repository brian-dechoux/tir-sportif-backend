package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class AddShotResultRequest {

    @NotNull
    @PositiveOrZero
    int serieNumber;

    /**
     * If null, it's a manual total value.
     */
    Integer shotNumber;

    @NotNull
    @PositiveOrZero
    double points;

}
