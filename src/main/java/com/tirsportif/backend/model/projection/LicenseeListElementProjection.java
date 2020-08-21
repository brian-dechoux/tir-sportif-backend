package com.tirsportif.backend.model.projection;

import com.tirsportif.backend.configuration.converter.LocalDateConverter;

import javax.persistence.Convert;
import java.time.LocalDate;

public interface LicenseeListElementProjection {

    Long getId();

    String getLastname();

    String getFirstname();

    @Convert(converter = LocalDateConverter.class)
    LocalDate getSubscriptionDate();

}
