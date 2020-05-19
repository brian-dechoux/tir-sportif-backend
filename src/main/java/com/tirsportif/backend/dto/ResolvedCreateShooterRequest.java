package com.tirsportif.backend.dto;

import com.tirsportif.backend.model.Category;
import com.tirsportif.backend.model.Club;
import com.tirsportif.backend.model.Country;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.Optional;

@Value
public class ResolvedCreateShooterRequest {

    @NonNull
    String lastname;

    @NonNull
    String firstname;

    LocalDate birthdate;

    ResolvedCreateAddressRequest address;

    Club club;

    @NonNull
    Category category;

    public static ResolvedCreateShooterRequest ofRawRequest(CreateShooterRequest request, @Nullable Country country, Club resolvedClub, Category resolvedCategory) {
        return new ResolvedCreateShooterRequest(
                request.getLastname(),
                request.getFirstname(),
                request.getBirthdate(),
                Optional.ofNullable(request.getAddress())
                    .map(address -> ResolvedCreateAddressRequest.ofRawRequest(request.getAddress(), country))
                .orElse(null),
                resolvedClub,
                resolvedCategory
        );
    }

}
