package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Participation;
import com.tirsportif.backend.model.projection.ParticipantProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    boolean existsByChallengeIdAndId(Long challengeId, Long participationId);

    List<Participation> findByChallengeIdAndShooterIdOrderByOutrankAsc(Long challengeId, Long shooterId);

    @Query(value = "SELECT DISTINCT(s.id), s.lastname, s.firstname, c.id as clubId, c.name as clubName " +
                "FROM participation p " +
                "INNER JOIN shooter s ON p.shooterId = s.id " +
                "LEFT JOIN club c ON s.clubId = c.id " +
                "WHERE p.challengeId = ?1"
            , countQuery = "SELECT COUNT(DISTINCT shooterId) " +
                "FROM participation p " +
                "WHERE p.challengeId = ?1"
            , nativeQuery = true)
    Page<ParticipantProjection> findParticipantsByChallengeId(Long challengeId, Pageable page);

}
