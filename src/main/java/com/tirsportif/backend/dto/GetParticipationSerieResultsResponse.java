package com.tirsportif.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class GetParticipationSerieResultsResponse {

    List<Double> points;

    /**
     * -1 shotNb result
     */
    Double calculatedTotal;

    /**
     * -2 shotNb result
     */
    Double manualTotal;

    public static GetParticipationSerieResultsResponse init(List<Double> points) {
        return new GetParticipationSerieResultsResponse(points, null, null);
    }

}
