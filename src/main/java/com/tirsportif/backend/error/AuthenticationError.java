package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthenticationError implements Error {

    EXPIRED_TOKEN("AUTH001", "L'authentification a expiré."),
    WRONG_FORMAT_TOKEN("AUTH002", "L'authentification a un mauvais format"),
    WRONG_USERNAME("AUTH003", "Nom d'utilisateur inconnu."),
    WRONG_PASSWORD("AUTH004", "Mauvais mot de passe."),
    UNKNOWN_TOKEN("AUTH005", "L'authentification a échoué.");

    private String code;
    private String message;

}
