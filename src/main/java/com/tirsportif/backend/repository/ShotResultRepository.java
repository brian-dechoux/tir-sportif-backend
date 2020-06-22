package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.ShotResult;
import com.tirsportif.backend.model.projection.ShotResultForCategoryAndDisciplineProjection;
import com.tirsportif.backend.model.projection.ShotResultProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShotResultRepository extends CrudRepository<ShotResult, Long> {

    boolean existsByParticipationId(Long participationId);

    @Query(value = "SELECT * FROM shotResult " +
            "WHERE participationId = ?1 " +
            "AND serieNumber = ?2 " +
            "ORDER BY shotNumber"
            , nativeQuery = true)
    List<ShotResult> findAllByParticipationIdAndSerieNumber(Long participationId, int serieNumber);

    @Query(value = "SELECT SUM(points) AS totalPoints, shooter.lastname, shooter.firstname FROM shotResult AS sr " +
            "INNER JOIN participation p ON sr.participationId = p.id " +
            "INNER JOIN shooter s ON p.shooterId = s.id " +
            "WHERE p.challengeId = ?1 " +
            "AND p.categoryId = ?2 " +
            "AND p.disciplineId = ?3 " +
            "GROUP BY lastname, firstname " +
            "ORDER BY totalPoints DESC"
            , nativeQuery = true)
    List<ShotResultForCategoryAndDisciplineProjection> getShotResultsForChallengeAndCategoryAndDiscipline(Long challengeId, Long categoryId, Long disciplineId);

    @Query(value = "SELECT p.id as participationId, p.outrank, sr.serieNumber, sr.shotNumber, sr.points FROM shotResult AS sr " +
            "RIGHT JOIN participation p ON sr.participationId = p.id " +
            "INNER JOIN shooter s ON p.shooterId = s.id " +
            "WHERE p.challengeId = ?1 " +
            "AND p.shooterId = ?2 " +
            "AND p.disciplineId = ?3 " +
            "ORDER BY p.outrank, p.id, sr.serieNumber, sr.shotNumber "
            , nativeQuery = true)
    List<ShotResultProjection> getShotResultsForChallengeAndShooterAndDiscipline(Long challengeId, Long shooterId, Long disciplineId);

    @Query(value = "SELECT p.id as participationId, p.outrank, p.useElectronicTarget, sr.serieNumber, sr.shotNumber, sr.points FROM shotResult AS sr " +
            "RIGHT JOIN participation p ON sr.participationId = p.id " +
            "WHERE p.challengeId = ?1 " +
            "AND p.id = ?2 " +
            "ORDER BY p.outrank, p.id, sr.serieNumber, sr.shotNumber "
            , nativeQuery = true)
    List<ShotResultProjection> getShotResultsForParticipation(Long challengeId, Long participationId);

}
