package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Challenge;
import com.tirsportif.backend.model.projection.ChallengeListElement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ChallengeRepository extends PagingAndSortingRepository<Challenge,Long> {

    @Query(value = "SELECT challenge.id, challenge.name, challenge.startDate, address.city, " +
            "       (" +
            "           SELECT COUNT(*) FROM participation " +
            "           WHERE participation.challengeId = challenge.id" +
            "       ) AS nbShooters " +
            "FROM challenge " +
            "INNER JOIN address ON address.id = challenge.addressId"
            , countQuery = "SELECT COUNT(*) FROM challenge"
            , nativeQuery = true)
    Page<ChallengeListElement> findAllAsListElements(Pageable page);

}
