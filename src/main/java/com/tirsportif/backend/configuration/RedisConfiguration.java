package com.tirsportif.backend.configuration;

import com.tirsportif.backend.property.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {

    private final RedisTemplate<String,String> redisTemplate;

    public RedisConfiguration(RedisTemplate<String,String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean(value = "stringOperations")
    public ValueOperations valueOperations() {
        return redisTemplate.opsForValue();
     }

}
