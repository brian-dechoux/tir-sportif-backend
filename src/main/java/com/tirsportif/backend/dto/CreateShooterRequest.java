package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class CreateShooterRequest {

    @NotNull
    @NotEmpty
    String lastname;

    @NotNull
    @NotEmpty
    String firstname;

    @Past
    LocalDate birthdate;

    @Valid
    CreateAddressRequest address;

    @Positive
    Long clubId;

    @NotNull
    @Positive
    Long categoryId;

}
