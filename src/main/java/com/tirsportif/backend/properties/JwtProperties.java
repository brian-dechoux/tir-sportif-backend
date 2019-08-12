package com.tirsportif.backend.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@Valid
@ConfigurationProperties(value = "tir-sportif.jwt")
public class JwtProperties {

    @NotEmpty
    String secret;

    @Positive
    int validity;

}
