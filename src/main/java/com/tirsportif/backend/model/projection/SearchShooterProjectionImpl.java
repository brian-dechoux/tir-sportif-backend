package com.tirsportif.backend.model.projection;

import lombok.Value;

@Value
public class SearchShooterProjectionImpl implements SearchShooterProjection {

    Long id;

    String lastname;

    String firstname;

    String clubName;

    String categoryLabel;

}
