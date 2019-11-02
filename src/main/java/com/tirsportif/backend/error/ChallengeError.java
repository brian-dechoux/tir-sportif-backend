package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChallengeError implements Error {

    PARTICIPATION_DISCIPLINE_NOT_AUTHORIZED_FOR_CHALLENGE("CH0001", "Cannot create participation for challenge with ID: %s, because the requested disciplines are not authorized.");

    private String code;
    private String message;

}
