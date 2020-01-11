package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class GetChallengeListElementResponse {

    @NonNull
    Long id;

    @NonNull
    String name;

    @NonNull
    LocalDateTime startDate;

    @NonNull
    String city;

    int nbShooters;

}
