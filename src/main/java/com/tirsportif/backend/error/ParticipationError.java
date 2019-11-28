package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ParticipationError implements Error {

    PARTICIPATION_DISCIPLINE_NOT_AUTHORIZED_FOR_CHALLENGE("PA0001", "Cannot create participation for challenge with ID: %s, because the requested disciplines are not authorized."),
    PARTICIPATION_DISCIPLINE_RANKED_SHOULD_BE_UNIQUE("PA0002", "Cannot create participation for challenge with ID: %s, there's already a ranked participation for provided discipline."),
    EXISTING_SHOT_RESULTS_FOR_PARTICIPATION("PA0003", "Cannot delete participation with ID: %s, there's already shot results associated."),
    PARTICIPATION_DOES_NOT_MATCH_CHALLENGE("PA0004", "Participation's challenge with ID: %s does not match challenge ID: %s.");

    private String code;
    private String message;

}
