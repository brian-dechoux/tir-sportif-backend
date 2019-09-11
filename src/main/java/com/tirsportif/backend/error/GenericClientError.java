package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GenericClientError implements Error {

    RESOURCE_NOT_FOUND("GCE0001", "Cannot find resource identified by the provided ID: %s");

    private String code;
    private String message;

}
