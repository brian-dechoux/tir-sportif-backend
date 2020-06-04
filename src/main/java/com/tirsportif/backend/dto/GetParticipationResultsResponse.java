package com.tirsportif.backend.dto;

import lombok.Value;

import java.util.List;

@Value
public class GetParticipationResultsResponse {

    ParticipationResultReferenceDto participationReference;
    List<List<Double>> points;

}
