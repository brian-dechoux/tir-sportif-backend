package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Shooter;
import com.tirsportif.backend.model.projection.SearchShooterProjection;
import com.tirsportif.backend.model.projection.ShooterListElementProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShooterRepository extends JpaRepository<Shooter,Long> {

    @Query(value = "SELECT s.id, s.lastname, s.firstname, c.name AS clubName, cat.label AS categoryLabel " +
            "FROM shooter s " +
            "LEFT JOIN club c ON s.clubId = c.id " +
            "INNER JOIN category cat ON s.categoryId = cat.id " +
            "WHERE CONCAT(s.firstname, ' ', s.lastname) LIKE CONCAT('%',?1,'%') " +
            "OR CONCAT(s.lastname, ' ', s.firstname) LIKE CONCAT('%',?1,'%')"
            , nativeQuery = true)
    List<SearchShooterProjection> search(String searchName);

    @Query(value = "SELECT s.id, s.lastname, s.firstname, cat.label AS categoryLabel " +
            "FROM shooter s " +
            "INNER JOIN category cat ON s.categoryId = cat.id " +
            "WHERE s.clubId = ?1 " +
            "ORDER BY s.lastname, s.firstname"
            , countQuery = "SELECT COUNT(*) FROM shooter WHERE clubId = ?1"
            , nativeQuery = true)
    Page<ShooterListElementProjection> findAllForClubAsListElements(Long clubId, Pageable page);

}
