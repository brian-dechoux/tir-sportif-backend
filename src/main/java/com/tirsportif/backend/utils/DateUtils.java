package com.tirsportif.backend.utils;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * All utility classes handling old {@link java.util.Date} from new {@link java.time} classes.
 */
public final class DateUtils {

    private final Clock clock;

    private DateUtils(Clock clock) {
        this.clock = clock;
    }

    /**
     * Clock is needed.
     * @param clock
     * @return Utils instance.
     */
    public static DateUtils fromClock(Clock clock) {
        return new DateUtils(clock);
    }

    public Date now() {
        return new Date(OffsetDateTime.now(clock).toInstant().toEpochMilli());
    }

    public Date nowPlus(long millis) {
        return new Date(OffsetDateTime.now(clock).toInstant().toEpochMilli() + millis);
    }

}
