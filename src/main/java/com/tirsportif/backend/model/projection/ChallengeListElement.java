package com.tirsportif.backend.model.projection;

import java.sql.Timestamp;

public interface ChallengeListElement {

    Long getId();

    String getName();

    Timestamp getStartDate();

    String getCity();

    int getNbShooters();

}
