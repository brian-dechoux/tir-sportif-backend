package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country,Long> {
}
