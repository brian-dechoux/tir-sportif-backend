package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.model.ShotResult;
import com.tirsportif.backend.model.projection.ShotResultForCategoryAndDisciplineProjection;
import com.tirsportif.backend.model.projection.ShotResultForShooterProjection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Mapping operations, using basic example:
     *
     * List of: 1 0 A B, 1 0 A C, 1 1 A B
     * to Map: 1 0 -> [(A,B), (A,C)], 1 1 -> [(A,B)]
     * to List: [(1 0 [(A,B), (A,C)]), (1 1 [(A,B)])]
     *
     * @param results
     * @return Mapped results
     */
    // FIXME Could it be done by some JPA magic ?
    public List<ParticipationResultsDto> mapShooterResultToDto(List<ShotResultForShooterProjection> results) {
        return results.stream()
                .collect(Collectors.groupingBy(
                        result -> new ParticipationResultReferenceDto(result.getParticipationId(), result.getOutrank()),
                        Collectors.mapping(this::mapShooterResultToDto, Collectors.toList())
                )).entrySet().stream()
                .map(entry -> new ParticipationResultsDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private ShooterResultDto mapShooterResultToDto(ShotResultForShooterProjection result) {
        return new ShooterResultDto(
                result.getSerieNumber(),
                result.getShotNumber(),
                result.getPoints()
        );
    }
}
