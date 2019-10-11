package com.tirsportif.backend.repository;

import com.tirsportif.backend.model.Challenge;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ChallengeRepository extends PagingAndSortingRepository<Challenge,Long> {

}
