package com.tirsportif.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * As we just use Redis to store JWT along with provided TTL feature, no need to hold a value.
 */
@Data
@RedisHash("token")
public class JwtTokenRedis {

    @Id
    String key;

    String value = "";

    @TimeToLive
    Long timeout;

    public JwtTokenRedis(String key, Long timeout) {
        this.key = key;
        this.timeout = timeout;
    }

}
