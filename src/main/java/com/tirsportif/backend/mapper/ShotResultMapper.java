package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.ResolvedAddShotResultRequest;
import com.tirsportif.backend.model.ShotResult;
import org.springframework.stereotype.Component;

@Component
public class ShotResultMapper {

    public ShotResult mapAddCreateShotResultDtoToShotResult(ResolvedAddShotResultRequest request) {
        ShotResult shotResult = new ShotResult();
        shotResult.setSerieNumber(request.getSerieNumber());
        shotResult.setShotNumber(request.getShotNumber());
        shotResult.setPoints(request.getPoints());
        shotResult.setParticipation(request.getParticipation());
        return shotResult;
    }

}
