package com.tirsportif.backend.model.projection;

public interface ShotResultProjection {

    Long getParticipationId();

    boolean getOutrank();

    boolean getUseElectronicTarget();

    /**
     * Null if participation without any shot result for the moment.
     */
    Integer getSerieNumber();

    /**
     * Null if:
     * - participation without any shot result for the moment
     * - OR if serie with only total points known
     */
    Integer getShotNumber();

    /**
     * Null if participation without any shot result for the moment.
     */
    Double getPoints();

}
