package com.tirsportif.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * As we just use Redis to store JWT along with provided TTL feature, no need to hold a value.
 */
@Data
@RedisHash("token")
public class JwtToken {

    @Id
    String key;
    String value = "";

}
