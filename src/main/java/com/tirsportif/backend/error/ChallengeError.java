package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChallengeError implements Error {

    PARTICIPATION_DISCIPLINE_NOT_AUTHORIZED_FOR_CHALLENGE("CH0001", "Cannot create participation for challenge with ID: %s, because the requested disciplines are not authorized."),
    PARTICIPATION_DISCIPLINE_RANKED_SHOULD_BE_UNIQUE("CH0002", "Cannot create participation for challenge with ID: %s, there's already a ranked participation for provided discipline.");

    private String code;
    private String message;

}
