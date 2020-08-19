package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.projection.SearchShooterProjection;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ShooterRepositoryCustom {

    List<SearchShooterProjection> search(String searchName, @Nullable Long clubId, @Nullable List<Long> categoryIds);

}
