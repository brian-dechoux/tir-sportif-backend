package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ParticipationError implements Error {

    PARTICIPATION_DISCIPLINE_NOT_AUTHORIZED_FOR_CHALLENGE("PA0001", "Discipline non autorisée pour le challenge: %s."),
    PARTICIPATION_DISCIPLINE_RANKED_SHOULD_BE_UNIQUE("PA0002", "Impossible d'ajouter la participation %s, il y a déjà une participation classée pour ce challenge."),
    EXISTING_SHOT_RESULTS_FOR_PARTICIPATION("PA0003", "Impossible de supprimer la participation %s, il y a des résultats associés à cette participation."),
    PARTICIPATION_DOES_NOT_MATCH_CHALLENGE("PA0004", "L'inscription %s ne correspond pas au challenge: %s.");

    private String code;
    private String message;

}
