package com.tirsportif.backend.dto;

import lombok.Value;

import java.time.LocalDate;

@Value
public class GetShooterResponse {

    Long id;

    String lastname;

    String firstname;

    LocalDate birthdate;

    GetAddressResponse address;

    GetClubResponse club;

    GetCategoryResponse category;

}
