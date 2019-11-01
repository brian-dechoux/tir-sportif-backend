package com.tirsportif.backend.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class GetParticipationsResponse {

    @NonNull
    Set<GetParticipationResponse> participations;

}
