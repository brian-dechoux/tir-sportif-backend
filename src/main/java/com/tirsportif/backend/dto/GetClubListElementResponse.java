package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

@Value
public class GetClubListElementResponse {

    @NonNull
    Long id;

    @NonNull
    String name;

    @NonNull
    String city;

    int nbShooters;

}
