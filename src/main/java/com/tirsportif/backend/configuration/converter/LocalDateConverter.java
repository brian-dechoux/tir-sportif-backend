package com.tirsportif.backend.configuration.converter;


import com.tirsportif.backend.configuration.TimeConfiguration;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

public class LocalDateConverter implements Converter<Timestamp, LocalDate> {

    @Override
    public LocalDate convert(Timestamp source) {
        long millisSinceEpoch = source.getTime();
        Instant instant = Instant.ofEpochMilli(millisSinceEpoch);
        return LocalDate.ofInstant(instant, TimeConfiguration.CURRENT_ZONE);
    }

}
