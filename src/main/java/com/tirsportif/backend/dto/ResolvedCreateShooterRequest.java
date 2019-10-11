package com.tirsportif.backend.dto;

import com.tirsportif.backend.model.Category;
import com.tirsportif.backend.model.Club;
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

    CreateAddressRequest address;

    Club club;

    @NonNull
    Category category;

    public static ResolvedCreateShooterRequest ofRawRequest(CreateShooterRequest request, Club resolvedClub, Category resolvedCategory) {
        return new ResolvedCreateShooterRequest(
                request.getLastname(),
                request.getFirstname(),
                request.getBirthdate(),
                request.getAddress(),
                resolvedClub,
                resolvedCategory
        );
    }

}
