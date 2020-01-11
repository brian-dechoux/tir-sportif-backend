package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Participation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParticipationRepository extends CrudRepository<Participation, Long> {

    boolean existsByChallengeIdAndId(Long challengeId, Long participationId);

    List<Participation> findByChallengeId(Long challengeId);

}
