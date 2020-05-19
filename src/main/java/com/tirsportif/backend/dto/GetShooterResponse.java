package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;

@Value
public class GetShooterResponse {

    @NonNull
    Long id;

    @NonNull
    String lastname;

    @NonNull
    String firstname;

    LocalDate birthdate;

    GetAddressResponse address;

    GetClubResponse club;

    GetCategoryResponse category;

}
