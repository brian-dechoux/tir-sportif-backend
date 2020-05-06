package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class GetChallengeListElementResponse {

    @NonNull
    Long id;

    @NonNull
    String name;

    @NonNull
    OffsetDateTime startDate;

    @NonNull
    String city;

    int nbShooters;

}
