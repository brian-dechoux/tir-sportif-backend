package com.tirsportif.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
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
