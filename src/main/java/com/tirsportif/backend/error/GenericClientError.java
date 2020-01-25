package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GenericClientError implements Error {

    RESOURCE_NOT_FOUND("GCE0001", "Cannot find resource identified by the provided ID: %s"),
    RESOURCES_NOT_FOUND("GCE0002", "Cannot find some resources in the provided list of IDs"),
    VALIDATION_FAILED("GCE0003", "Provided params for request are not valid: %s");

    private String code;
    private String message;

}
