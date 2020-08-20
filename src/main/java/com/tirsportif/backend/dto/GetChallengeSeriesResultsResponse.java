package com.tirsportif.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class GetChallengeSeriesResultsResponse {

    String lastname;

    String firstname;

    List<Double> participationSeriesPoints;

    double participationTotalPoints;

}
