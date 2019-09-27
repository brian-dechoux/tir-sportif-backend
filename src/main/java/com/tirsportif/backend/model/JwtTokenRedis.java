package com.tirsportif.backend.model;

import com.tirsportif.backend.model.redis.RedisModel;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.TimeToLive;

/**
 * As we just use Redis to store JWT along with provided TTL feature, no need to hold a value.
 * use value operations
 */
@Data
public class JwtTokenRedis implements RedisModel {

    @Id
    @NonNull
    String key;

    @NonNull
    String value;

    @TimeToLive
    @NonNull
    Long timeout;

    public JwtTokenRedis(String baseKey, String username, Long timeout) {
        this.key = formatKey(baseKey);
        this.value = username;
        this.timeout = timeout;
    }

    @Override
    public String formatKey(String baseKey) {
        return "token:" + baseKey;
    }

}
