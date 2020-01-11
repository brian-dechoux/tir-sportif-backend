package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CreateChallengeRequest {

    @NotNull
    @NotEmpty
    String name;

    @NotNull
    @Future
    LocalDateTime startDate;

    @NotNull
    @Valid
    CreateAddressRequest address;

    @NotNull
    @Positive
    Long organiserClubId;

    @NotEmpty
    Set<@Positive @NotNull Long> categoryIds;

    @NotEmpty
    Set<@Positive @NotNull Long> disciplineIds;

}
