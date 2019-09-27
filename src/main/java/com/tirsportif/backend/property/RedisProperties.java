package com.tirsportif.backend.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@ConfigurationProperties(value = "spring.redis")
public class RedisProperties {

    @NotNull
    TimeUnit timeUnit;

}
