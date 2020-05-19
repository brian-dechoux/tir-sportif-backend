package com.tirsportif.backend.dto;

import lombok.Value;

import java.util.List;

@Value
public class ParticipationResultsDto {

    ParticipationResultReferenceDto participationReference;
    List<List<Double>> points;

}
