package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.ShotResult;
import com.tirsportif.backend.model.projection.ShotResultForCategoryAndDisciplineProjection;
import com.tirsportif.backend.model.projection.ShotResultForShooterProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShotResultRepository extends CrudRepository<ShotResult, Long> {

    boolean existsByParticipationId(Long participationId);

    @Query(value = "SELECT SUM(points) AS totalPoints, shooter.lastname, shooter.firstname FROM shotResult " +
            "INNER JOIN participation ON shotResult.participationId = participation.id " +
            "INNER JOIN shooter ON participation.shooterId = shooter.id " +
            "WHERE participation.challengeId = ?1 " +
            "AND participation.categoryId = ?2 " +
            "AND participation.disciplineId = ?3 " +
            "GROUP BY lastname, firstname " +
            "ORDER BY totalPoints DESC"
            , nativeQuery = true)
    List<ShotResultForCategoryAndDisciplineProjection> getShotResultsForChallengeAndCategoryAndDiscipline(Long challengeId, Long categoryId, Long disciplineId);

    @Query(value = "SELECT sr.serieNumber, sr.shotNumber, sr.points FROM shotResult AS sr " +
            "INNER JOIN participation ON sr.participationId = participation.id " +
            "INNER JOIN shooter ON participation.shooterId = shooter.id " +
            "WHERE participation.challengeId = ?1 " +
            "AND participation.shooterId = ?2 "
            , nativeQuery = true)
    List<ShotResultForShooterProjection> getShotResultsForChallengeAndShooter(Long challengeId, Long shooterId);

}
