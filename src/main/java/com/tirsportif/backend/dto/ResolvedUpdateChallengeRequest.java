package com.tirsportif.backend.dto;

import com.tirsportif.backend.model.Category;
import com.tirsportif.backend.model.Club;
import com.tirsportif.backend.model.Country;
import com.tirsportif.backend.model.Discipline;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "OptionalGetWithoutIsPresent"})
@Value
@AllArgsConstructor
public class ResolvedUpdateChallengeRequest {

    Optional<String> name;

    Optional<OffsetDateTime> startDate;

    Optional<ResolvedCreateAddressRequest> address;

    Optional<Club> organiserClub;

    Optional<Set<Category>> categories;

    Optional<Set<Discipline>> disciplines;

    public static ResolvedUpdateChallengeRequest ofRawRequest(UpdateChallengeRequest request, Optional<Country> optCountry, Optional<Club> optClub, Optional<Set<Category>> optCategories, Optional<Set<Discipline>> optDisciplines) {
        Optional<CreateAddressRequest> optAddress = Optional.empty();
        if (request.getAddress() != null && optCountry.isPresent()) {
            optAddress = Optional.of(request.getAddress());
        }
        return new ResolvedUpdateChallengeRequest(
                Optional.ofNullable(request.getName()),
                Optional.ofNullable(request.getStartDate()),
                optAddress.map(address -> ResolvedCreateAddressRequest.ofRawRequest(address, optCountry.get())),
                optClub,
                optCategories,
                optDisciplines
        );
    }

}
