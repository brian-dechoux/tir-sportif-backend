package com.tirsportif.backend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class GetParticipationResponse {

    Long id;

    @NonNull
    GetShooterResponse shooter;

    @NonNull
    GetChallengeResponse challenge;

    @NonNull
    GetCategoryResponse category;

    @NonNull
    GetDisciplineResponse discipline;

    boolean useElectronicTarget;

    boolean paid;

    boolean outrank;

}
