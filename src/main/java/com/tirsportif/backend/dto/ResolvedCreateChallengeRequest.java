package com.tirsportif.backend.dto;

import com.tirsportif.backend.model.Category;
import com.tirsportif.backend.model.Club;
import com.tirsportif.backend.model.Country;
import com.tirsportif.backend.model.Discipline;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.Set;

@Value
public class ResolvedCreateChallengeRequest {

    @NonNull
    String name;

    @NonNull
    OffsetDateTime startDate;

    @NonNull
    ResolvedCreateAddressRequest address;

    @NonNull
    Club organiserClub;

    @NonNull
    Set<Category> categories;

    @NonNull
    Set<Discipline> disciplines;

    public static ResolvedCreateChallengeRequest ofRawRequest(CreateChallengeRequest request, Country resolvedCountry, Club resolvedClub, Set<Category> resolvedCategories, Set<Discipline> resolvedDisciplines) {
        return new ResolvedCreateChallengeRequest(
                request.getName(),
                request.getStartDate(),
                ResolvedCreateAddressRequest.ofRawRequest(request.getAddress(), resolvedCountry),
                resolvedClub,
                resolvedCategories,
                resolvedDisciplines
        );
    }

}
