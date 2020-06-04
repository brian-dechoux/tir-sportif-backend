package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SystemError implements Error {

    TECHNICAL_ERROR("SYS001", "Une erreur technique est survenue.");

    private String code;
    private String message;

}
