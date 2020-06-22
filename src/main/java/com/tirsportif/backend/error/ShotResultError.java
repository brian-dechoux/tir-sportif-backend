package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ShotResultError implements Error {

    INVALID_SERIE_NUMBER("SR0001", "Mauvais numéro de serie: %s. Devrait être positif et inférieur à: %s"),
    INVALID_SHOT_NUMBER("SR0002", "Mauvais numéro de tir: %s. Devrait être positif et inférieur à: %s"),
    INVALID_POINTS_VALUE("SR0003", "La discipline actuelle n'accepte pas les nombres à virgule"),
    SHOT_RESULT_MUST_BE_UNIQUE("SR0004", "Les résultats doivent être uniques."),
    OUTRANGE_POINTS_VALUE("SR0005", "Les résultats doivent être entre (inclusif) %s et %s"),
    OUTRANGE_TOTAL_POINTS_VALUE("SR0006", "Le total doit être entre (inclusif) %s et %s "),
    INVALID_SHOT_NUMBER_OUTBOUNDS("SR0007", "Numéro de tir doit être supérieur ou égal à -2");

    private String code;
    private String message;

}
