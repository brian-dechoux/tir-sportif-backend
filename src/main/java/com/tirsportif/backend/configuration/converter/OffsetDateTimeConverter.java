package com.tirsportif.backend.configuration.converter;


import com.tirsportif.backend.configuration.TimeConfiguration;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;

public class OffsetDateTimeConverter implements Converter<Timestamp, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(Timestamp source) {
        long millisSinceEpoch = source.getTime();
        Instant instant = Instant.ofEpochMilli(millisSinceEpoch);
        return OffsetDateTime.ofInstant(instant, TimeConfiguration.CURRENT_ZONE);
    }

}
