package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Challenge;
import com.tirsportif.backend.model.projection.ChallengeListElementProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ChallengeRepository extends PagingAndSortingRepository<Challenge,Long> {

    @Query(value = "SELECT challenge.id, challenge.name, challenge.startDate, address.city, COUNT(DISTINCT p.shooterId) AS nbShooters " +
            "FROM challenge " +
            "INNER JOIN address ON address.id = challenge.addressId " +
            "LEFT JOIN participation p on challenge.id = p.challengeId " +
            "GROUP BY challenge.id, challenge.name, challenge.startDate, address.city " +
            "ORDER BY challenge.startDate, challenge.id DESC"
            , countQuery = "SELECT COUNT(*) FROM challenge"
            , nativeQuery = true)
    Page<ChallengeListElementProjection> findAllAsListElements(Pageable page);

}
