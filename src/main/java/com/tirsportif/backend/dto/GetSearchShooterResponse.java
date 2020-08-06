package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

@Value
public class GetSearchShooterResponse {

    @NonNull
    Long id;

    @NonNull
    String lastname;

    @NonNull
    String firstname;

    String clubName;

    @NonNull
    String categoryLabel;

}
