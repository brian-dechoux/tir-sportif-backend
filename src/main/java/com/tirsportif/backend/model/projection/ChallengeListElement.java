package com.tirsportif.backend.model.projection;

import java.time.LocalDateTime;

public interface ChallengeListElement {

    Long getId();

    String getName();

    LocalDateTime getStartDate();

    String getCity();

    int getNbShooters();

}
