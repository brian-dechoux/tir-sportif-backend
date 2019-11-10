package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Participation;
import org.springframework.data.repository.CrudRepository;

public interface ParticipationRepository extends CrudRepository<Participation, Long> {

    boolean existsByChallengeIdAndId(Long challengeId, Long participationId);

}
