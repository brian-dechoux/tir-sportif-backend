package com.tirsportif.backend.dto;

import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.exception.BadRequestErrorException;
import com.tirsportif.backend.model.Category;
import com.tirsportif.backend.model.Club;
import com.tirsportif.backend.utils.Regexes;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;

@Value
public class ResolvedCreateShooterRequest {

    @NonNull
    String lastname;

    @NonNull
    String firstname;

    LocalDate birthdate;

    Club club;

    @NonNull
    Category category;

    String email;

    public static ResolvedCreateShooterRequest ofRawRequest(CreateShooterRequest request, Club resolvedClub, Category resolvedCategory) {
        if (request.getEmail() != null && !request.getEmail().matches(Regexes.EMAIL)) {
            throw new BadRequestErrorException(GenericClientError.VALIDATION_FAILED, "email");
        }
        return new ResolvedCreateShooterRequest(
                request.getLastname(),
                request.getFirstname(),
                request.getBirthdate(),
                resolvedClub,
                resolvedCategory,
                request.getEmail()
        );
    }

}
