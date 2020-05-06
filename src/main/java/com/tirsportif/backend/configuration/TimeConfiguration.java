package com.tirsportif.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class TimeConfiguration {

    public static final ZoneId CURRENT_ZONE = ZoneId.of("Europe/Paris");

    @Bean
    public Clock clock() {
        return Clock.system(CURRENT_ZONE);
    }

}
