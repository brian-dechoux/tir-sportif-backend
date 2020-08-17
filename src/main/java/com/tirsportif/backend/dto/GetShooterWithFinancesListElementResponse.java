package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

@Value
public class GetShooterWithFinancesListElementResponse {

    @NonNull
    Long id;

    @NonNull
    String lastname;

    @NonNull
    String firstname;

}
