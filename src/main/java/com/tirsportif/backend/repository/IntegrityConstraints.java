package com.tirsportif.backend.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IntegrityConstraints {

    PARTICIPATION_DISCIPLINE_ONLY_ONE_RANKED("participationDisciplineOnlyOneRankedUniqueIndex"),
    SHOT_RESULT_UNIQUE("shotResultUniqueIndex");

    private String causeMessagePart;

}
