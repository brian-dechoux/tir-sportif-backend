package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Licensee;
import com.tirsportif.backend.model.projection.LicenseeListElementProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LicenseeRepository extends JpaRepository<Licensee,Long> {

    Optional<Licensee> findByShooterId(Long shooterId);

    @Query(value = "SELECT licensee.id, shooter.lastname, shooter.firstname, licensee.subscriptionDate " +
            "FROM licensee " +
            "INNER JOIN shooter ON shooter.id = licensee.shooterId " +
            "ORDER BY CONCAT(shooter.lastname, ' ', shooter.firstname)"
            , countQuery = "SELECT COUNT(*) FROM licensee"
            , nativeQuery = true)
    Page<LicenseeListElementProjection> findAllAsListElements(Pageable page);

}
