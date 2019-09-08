package com.tirsportif.backend.configuration;

import com.tirsportif.backend.property.ApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApiProperties.class)
public class ApiConfiguration {
}
