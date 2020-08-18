package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Club;
import com.tirsportif.backend.model.projection.ClubListElementProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClubRepository extends JpaRepository<Club,Long> {

    @Query(value = "SELECT c.id, c.name, a.city, COUNT(s.id) AS nbShooters " +
            "FROM club c " +
            "INNER JOIN address a on c.addressId = a.id " +
            "LEFT JOIN shooter s on c.id = s.clubId " +
            "WHERE c.id <> ?1 " +
            "GROUP BY c.id, c.name, a.city " +
            "ORDER BY c.name"
            , countQuery = "SELECT COUNT(*) FROM challenge"
            , nativeQuery = true)
    Page<ClubListElementProjection> findAllAsListElements(Pageable page, Long myClubId);

}
