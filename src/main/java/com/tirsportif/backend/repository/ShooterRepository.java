package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Shooter;
import com.tirsportif.backend.model.projection.ShooterListElementProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShooterRepository extends JpaRepository<Shooter,Long>, ShooterRepositoryCustom {

    @Query(value = "SELECT s.id, s.lastname, s.firstname, cat.label AS categoryLabel " +
            "FROM shooter s " +
            "INNER JOIN category cat ON s.categoryId = cat.id " +
            "WHERE s.clubId = ?1 " +
            "ORDER BY s.lastname, s.firstname"
            , countQuery = "SELECT COUNT(*) FROM shooter WHERE clubId = ?1"
            , nativeQuery = true)
    Page<ShooterListElementProjection> findAllForClubAsListElements(Long clubId, Pageable page);

}
