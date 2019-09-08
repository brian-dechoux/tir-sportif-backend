package com.tirsportif.backend.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Data
@Valid
@ConfigurationProperties(value = "tir-sportif.api")
public class ApiProperties {

    @Positive
    int paginationSize;

}
