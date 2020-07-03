package com.tirsportif.backend.dto;

import com.tirsportif.backend.utils.Regexes;
import lombok.Data;

import javax.validation.constraints.*;
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

    @Positive
    Long clubId;

    @NotNull
    @Positive
    Long categoryId;

    @Email(regexp = Regexes.EMAIL)
    String email;
}
