package com.tirsportif.backend.dto;

import com.tirsportif.backend.model.Category;
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

    Category category;

}
