package com.tirsportif.backend.model.projection;

public interface ShotResultForShooterProjection {

    Long getParticipationId();

    boolean getOutrank();

    int getSerieNumber();

    Integer getShotNumber();

    double getPoints();

}
