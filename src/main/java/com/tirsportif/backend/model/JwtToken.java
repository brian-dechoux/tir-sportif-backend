package com.tirsportif.backend.model;

import lombok.Value;
import org.springframework.data.redis.core.RedisHash;

@Value
@RedisHash("token")
public class JwtToken {

    String value;

}
