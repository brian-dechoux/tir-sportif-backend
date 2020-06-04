package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.model.Discipline;
import com.tirsportif.backend.model.ShotResult;
import com.tirsportif.backend.model.projection.ShotResultForCategoryAndDisciplineProjection;
import com.tirsportif.backend.model.projection.ShotResultForShooterProjection;
import org.springframework.stereotype.Component;

import java.util.*;
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
     * to List: [(1 0 [[A.p, B.p], [A.p, C.p]]), (1 1 [[A.p, B.p]])]
     *
     * There's one special case in this workflow, the case of a shot result with only the total.
     * We set the total at the last index in the list (discipline.nbShots, considering index 0)
     * Imagine a table like this to picture:
     * +-------------------+-------------------+--------+
     * | Serie1Shot1Result | Serie1Shot2Result | Total1 |
     * | Serie2Shot1Result | Serie2Shot2Result | Total2 |
     * +-------------------+-------------------+--------+
     *
     * Note: Specific List and Map implementation are used to preserve ordering.
     *
     * @param results
     * @param discipline
     *
     * @return Mapped results
     */
    // FIXME Could it be done by some JPA magic ?
    public List<ParticipationResultsDto> mapShooterResultToDto(List<ShotResultForShooterProjection> results, Discipline discipline) {
        return results.stream()
                .collect(Collectors.groupingBy(
                        result -> new ParticipationResultReferenceDto(result.getParticipationId(), result.getOutrank()),
                        LinkedHashMap::new,
                        Collectors.mapping(this::mapShooterResultToDto, Collectors.toCollection(LinkedList::new))
                )).entrySet().stream()
                .map(entry -> {
                    List<List<Double>> points = new ArrayList<>();
                    for (ShooterResultDto singleResult : entry.getValue()) {
                        // Special case
                        if (singleResult.getShotNumber() == null) {
                            List<Double> onlyTotalResults = new ArrayList<>(discipline.getNbShotsPerSerie() + 1);
                            onlyTotalResults.addAll(Collections.nCopies(discipline.getNbShotsPerSerie() + 1, null));
                            onlyTotalResults.set(discipline.getNbShotsPerSerie(), singleResult.getPoints());
                            points.add(onlyTotalResults);
                        } else if (singleResult.getShotNumber() == 0) {
                            List<Double> serieResults = new ArrayList<>(discipline.getNbShotsPerSerie());
                            serieResults.add(singleResult.getPoints());
                            points.add(serieResults);
                        } else {
                            if (points.size() == 0) {
                                List<Double> serieResults = new ArrayList<>(discipline.getNbShotsPerSerie());
                                points.add(serieResults);
                            }
                            points.get(points.size()-1).add(singleResult.getPoints());
                        }
                    }
                    return new ParticipationResultsDto(entry.getKey(), points);
                })
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private ShooterResultDto mapShooterResultToDto(ShotResultForShooterProjection result) {
        return new ShooterResultDto(
                result.getSerieNumber(),
                result.getShotNumber(),
                result.getPoints()
        );
    }
}
