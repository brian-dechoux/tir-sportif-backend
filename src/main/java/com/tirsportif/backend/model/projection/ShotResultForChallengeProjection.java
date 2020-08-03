package com.tirsportif.backend.model.projection;

public interface ShotResultForChallengeProjection {

    String getLastname();

    String getFirstname();

    long getCategoryId();

    String getCategoryLabel();

    long getDisciplineId();

    String getDisciplineLabel();

    long getParticipationId();

    double getParticipationTotalPoints();

}
