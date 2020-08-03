package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Value
public class GetChallengeResponse {

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
    List<GetCategoryResponse> categories;

    @NonNull
    List<GetDisciplineResponse> disciplines;

}
