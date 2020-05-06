package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Participation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ParticipationRepository extends CrudRepository<Participation, Long> {

    boolean existsByChallengeIdAndId(Long challengeId, Long participationId);

    Page<Participation> findByChallengeId(Long challengeId, Pageable page);

}
