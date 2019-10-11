package com.tirsportif.backend.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@Data
@Validated
@ConfigurationProperties(value = "spring.redis")
public class RedisProperties {

    @NotNull
    TimeUnit timeUnit;

}
