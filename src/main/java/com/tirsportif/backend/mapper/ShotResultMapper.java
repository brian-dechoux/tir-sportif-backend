package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.CategoryAndDisciplineResultDto;
import com.tirsportif.backend.dto.ResolvedAddShotResultRequest;
import com.tirsportif.backend.dto.ShooterResultDto;
import com.tirsportif.backend.model.ShotResult;
import com.tirsportif.backend.model.projection.ShotResultForCategoryAndDisciplineProjection;
import com.tirsportif.backend.model.projection.ShotResultForShooterProjection;
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

    public CategoryAndDisciplineResultDto mapCategoryResultToDto(ShotResultForCategoryAndDisciplineProjection result) {
        return new CategoryAndDisciplineResultDto(
                result.getTotalPoints(),
                result.getLastname(),
                result.getFirstname()
        );
    }

    public ShooterResultDto mapShooterResultToDto(ShotResultForShooterProjection result) {
        return new ShooterResultDto(
                result.getSerieNumber(),
                result.getShotNumber(),
                result.getPoints()
        );
    }

}
