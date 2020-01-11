package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

@Value
public class GetChallengeResponse {

    @NonNull
    Long id;

    @NonNull
    String name;

    @NonNull
    LocalDateTime startDate;

    @NonNull
    GetAddressResponse address;

    @NonNull
    GetClubResponse club;

    @NonNull
    Set<GetCategoryResponse> categories;

    @NonNull
    Set<GetDisciplineResponse> disciplines;

}
