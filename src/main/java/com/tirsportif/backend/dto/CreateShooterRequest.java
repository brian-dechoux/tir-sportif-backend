package com.tirsportif.backend.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@Validated
public class CreateShooterRequest {

    @NotNull
    @NotEmpty
    String lastname;

    @NotNull
    @NotEmpty
    String firstname;

    @Past
    LocalDate birthdate;

    CreateAddressRequest address;

    @Positive
    Long clubId;

    @NotNull
    @Positive
    Long categoryId;

}
