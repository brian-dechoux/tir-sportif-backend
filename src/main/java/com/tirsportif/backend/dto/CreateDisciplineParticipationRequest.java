package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class CreateDisciplineParticipationRequest {

    @NotNull
    @Positive
    Long disciplineId;

    boolean useElectronicTarget;

    boolean paid;

    boolean outrank;

}
