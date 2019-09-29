package com.tirsportif.backend.model.redis;

public interface RedisKey {

    String formatKey(String baseKey);

    String getFormattedKey();

}
