package com.tirsportif.backend.dto;

import com.tirsportif.backend.model.Category;
import lombok.Value;

import java.time.LocalDate;

@Value
public class GetShooterResponse {

    String lastname;

    String firstname;

    LocalDate birthdate;

    GetAddressResponse address;

    GetClubResponse clubResponse;

    Category category;

}
