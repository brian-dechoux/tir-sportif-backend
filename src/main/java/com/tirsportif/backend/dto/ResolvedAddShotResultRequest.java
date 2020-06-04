package com.tirsportif.backend.dto;

import com.tirsportif.backend.model.Participation;
import lombok.NonNull;
import lombok.Value;

@Value
public class ResolvedAddShotResultRequest {

    int serieNumber;

    int shotNumber;

    double points;

    @NonNull
    Participation participation;

    public static ResolvedAddShotResultRequest ofRawRequest(AddShotResultRequest request, Participation participation) {
        return new ResolvedAddShotResultRequest(
                request.getSerieNumber(),
                request.getShotNumber(),
                request.getPoints(),
                participation
        );
    }

}
