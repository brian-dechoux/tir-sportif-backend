package com.tirsportif.backend.model.redis;

import lombok.Getter;

@Getter
public class JwtTokenKey implements RedisKey {

    private final String formattedKey;

    public JwtTokenKey(String key) {
        this.formattedKey = formatKey(key);
    }

    @Override
    public String formatKey(String baseKey) {
        return "token:" + baseKey;
    }

}
