package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.JwtTokenRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtRepository extends CrudRepository<JwtTokenRedis, String> {
}
