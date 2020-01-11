package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UpdateChallengeRequest {

    String name;

    @Future
    LocalDateTime startDate;

    @Valid
    CreateAddressRequest address;

    @Positive
    Long organiserClubId;

    Set<@Positive Long> categoryIds;

    Set<@Positive Long> disciplineIds;

}
