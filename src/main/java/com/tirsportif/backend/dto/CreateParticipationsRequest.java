package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@Data
public class CreateParticipationsRequest {

    @NotNull
    @Positive
    Long shooterId;

    @Valid
    Set<CreateDisciplineParticipationRequest> disciplinesInformation;

}
