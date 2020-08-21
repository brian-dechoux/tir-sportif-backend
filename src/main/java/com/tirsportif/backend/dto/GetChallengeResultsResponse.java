package com.tirsportif.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class GetChallengeResultsResponse {

    GetChallengeResponse challenge;

    List<GetChallengeCategoryDisciplineResultsResponse> challengeResults;

}
