package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GenericClientError implements Error {

    RESOURCE_NOT_FOUND("GCE0001", "Ressource introuvable: %s"),
    RESOURCES_NOT_FOUND("GCE0002", "Certaines ressources introuvables"),
    VALIDATION_FAILED("GCE0003", "Le paramètre suivant est invalide: %s");

    private String code;
    private String message;

}
