package com.tirsportif.backend.model.projection;

import com.tirsportif.backend.configuration.converter.OffsetDateTimeConverter;

import javax.persistence.Convert;
import java.time.OffsetDateTime;

public interface ChallengeListElement {

    Long getId();

    String getName();

    @Convert(converter = OffsetDateTimeConverter.class)
    OffsetDateTime getStartDate();

    String getCity();

    int getNbShooters();

}
