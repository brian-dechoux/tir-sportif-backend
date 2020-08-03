package com.tirsportif.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class GetChallengeCategoryDisciplineResultResponse {

    String lastname;

    String firstname;

    long participationId;

    double participationTotalPoints;

}
