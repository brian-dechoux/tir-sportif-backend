package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

import java.sql.Timestamp;

@Value
public class GetChallengeListElementResponse {

    @NonNull
    Long id;

    @NonNull
    String name;

    @NonNull
    Timestamp startDate;

    @NonNull
    String city;

    int nbShooters;

}
