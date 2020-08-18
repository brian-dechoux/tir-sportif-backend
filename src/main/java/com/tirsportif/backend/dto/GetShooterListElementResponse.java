package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

@Value
public class GetShooterListElementResponse {

    @NonNull
    Long id;

    @NonNull
    String lastname;

    @NonNull
    String firstname;

    @NonNull
    String categoryLabel;

}
