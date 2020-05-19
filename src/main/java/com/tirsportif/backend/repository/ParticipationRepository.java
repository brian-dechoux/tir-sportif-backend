package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Participation;
import com.tirsportif.backend.model.projection.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ParticipationRepository extends PagingAndSortingRepository<Participation, Long> {

    boolean existsByChallengeIdAndId(Long challengeId, Long participationId);

    List<Participation> findByChallengeIdAndShooterId(Long challengeId, Long shooterId);

    @Query(value = "SELECT DISTINCT(s.id), s.lastname, s.firstname, c.id as clubId, c.name as clubName " +
                "FROM participation p " +
                "INNER JOIN shooter s on p.shooterId = s.id " +
                "LEFT JOIN club c ON (s.clubId = c.id) " +
                "WHERE p.challengeId = ?1"
            , countQuery = "SELECT COUNT(DISTINCT shooterId) " +
                "FROM participation p " +
                "WHERE p.challengeId = ?1"
            , nativeQuery = true)
    Page<Participant> findParticipantsByChallengeId(Long challengeId, Pageable page);

}
