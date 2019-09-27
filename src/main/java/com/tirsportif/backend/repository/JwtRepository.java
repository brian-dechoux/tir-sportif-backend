package com.tirsportif.backend.repository;

import org.springframework.data.redis.core.ValueOperations;

public interface JwtRepository extends ValueOperations<String, String> {
}
