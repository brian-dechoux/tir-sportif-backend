package com.tirsportif.backend.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@Valid
@ConfigurationProperties(value = "tir-sportif.jwt")
public class JwtProperties {

    /**
     * Necessary secret to add entropy into JWT token generation.
     */
    @NotEmpty
    String secret;

    /**
     * Ms validity of the token.
     */
    @Positive
    long validity;

}
