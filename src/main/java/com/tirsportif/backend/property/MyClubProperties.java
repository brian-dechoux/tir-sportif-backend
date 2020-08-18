package com.tirsportif.backend.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Positive;

@Data
@Validated
@ConfigurationProperties(value = "tir-sportif.my-club")
public class MyClubProperties {

    @Positive
    long id;

}
