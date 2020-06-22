package com.tirsportif.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ParticipationResultReferenceDto {

    Long participationId;

    int nbShotsPerSerie;

    boolean outrank;

    boolean useElectronicTarget;

}
