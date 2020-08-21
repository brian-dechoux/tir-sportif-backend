package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthenticationError implements Error {

    EXPIRED_TOKEN("AUTH001", "L'authentification a expiré"),
    WRONG_FORMAT_TOKEN("AUTH002", "L'authentification a un mauvais format"),
    WRONG_CREDENTIALS("AUTH003", "Identifiants inconnus"),
    UNKNOWN_TOKEN("AUTH004", "L'authentification a échoué");

    private String code;
    private String message;

}
