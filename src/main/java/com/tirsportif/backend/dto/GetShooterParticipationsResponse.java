package com.tirsportif.backend.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GetShooterParticipationsResponse {

    @NonNull
    GetShooterResponse shooter;

    List<GetParticipationResponse> participations;

}
