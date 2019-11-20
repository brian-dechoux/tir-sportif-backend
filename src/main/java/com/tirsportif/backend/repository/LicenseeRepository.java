package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Licensee;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LicenseeRepository extends CrudRepository<Licensee,Long> {

    Optional<Licensee> findByShooterId(Long shooterId);

}
