package com.tirsportif.backend.configuration;

import com.tirsportif.backend.property.MyClubProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MyClubProperties.class)
public class MyClubConfiguration {
}
