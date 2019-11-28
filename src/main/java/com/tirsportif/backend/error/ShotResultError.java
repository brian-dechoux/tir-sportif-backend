package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ShotResultError implements Error {

    INVALID_SERIE_NUMBER("SR0001", "Invalid serie number: %s. Should be positive and lighter than: %s"),
    INVALID_SHOT_NUMBER("SR0002", "Invalid shot number: %s. Should be positive and lighter than: %s"),
    INVALID_POINTS_VALUE("SR0003", "Invalid points value: %s, based on discipline parameters."),
    SHOT_RESULT_MUST_BE_UNIQUE("SR0004", "Shot results must be unique among serie number, shot number and participationId.");

    private String code;
    private String message;

}
