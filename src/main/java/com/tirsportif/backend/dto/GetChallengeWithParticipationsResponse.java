package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.Set;

@Value
public class GetChallengeWithParticipationsResponse {

    @NonNull
    Long id;

    @NonNull
    String name;

    @NonNull
    OffsetDateTime startDate;

    @NonNull
    GetAddressResponse address;

    @NonNull
    GetClubResponse club;

    @NonNull
    Set<GetCategoryResponse> categories;

    @NonNull
    Set<GetDisciplineResponse> disciplines;

    @NonNull
    Set<GetParticipationResponse> participations;

}
