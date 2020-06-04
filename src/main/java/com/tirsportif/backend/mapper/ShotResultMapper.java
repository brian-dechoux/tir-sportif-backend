package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.model.Discipline;
import com.tirsportif.backend.model.ShotResult;
import com.tirsportif.backend.model.key.ShotResultKey;
import com.tirsportif.backend.model.projection.ShotResultForCategoryAndDisciplineProjection;
import com.tirsportif.backend.model.projection.ShotResultProjection;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ShotResultMapper {

    public ShotResult mapAddCreateShotResultDtoToShotResult(ResolvedAddShotResultRequest request) {
        ShotResult shotResult = new ShotResult();
        shotResult.setId(new ShotResultKey(request.getSerieNumber(), request.getShotNumber(), request.getParticipation().getId()));
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
     * Total is always calculated for all shot results.
     * If no result for a serie, an empty results list will be provided.
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
    public List<GetParticipationResultsResponse> mapResultToDto(List<ShotResultProjection> results, Discipline discipline) {
        return results.stream()
                .collect(Collectors.groupingBy(
                        result -> new ParticipationResultReferenceDto(result.getParticipationId(), result.getOutrank()),
                        LinkedHashMap::new,
                        Collectors.mapping(this::mapResultToDto, Collectors.toCollection(LinkedList::new))
                )).entrySet().stream()
                .map(participationCurrentEntry -> formatResultsResponse(participationCurrentEntry, discipline))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private GetParticipationResultsResponse formatResultsResponse(Map.Entry<ParticipationResultReferenceDto, LinkedList<ShooterResultDto>> participationCurrentEntry, Discipline discipline) {
        // Initialize points with null values in case of no shot results for a serie for example
        List<List<Double>> points = new ArrayList<>(discipline.getNbSeries());
        points.addAll(Collections.nCopies(discipline.getNbSeries(), initializedSerieResultList(discipline)));

        int currentSerieShotNb = 0;
        for (ShooterResultDto singleResult : participationCurrentEntry.getValue()) {
            if (singleResult.getSerieNumber() != null) {
                if (singleResult.getShotNumber() == null || singleResult.getShotNumber() == 0) {
                    // Special case
                    if (singleResult.getShotNumber() == null) {
                        List<Double> onlyTotalResults = initializedSerieResultList(discipline);
                        onlyTotalResults.set(discipline.getNbShotsPerSerie(), singleResult.getPoints());
                        points.set(singleResult.getSerieNumber() - 1, onlyTotalResults);
                    // New serie
                    } else {
                        currentSerieShotNb = 0;
                        List<Double> serieResults = initializedSerieResultList(discipline);
                        serieResults.set(currentSerieShotNb, singleResult.getPoints());
                        points.set(singleResult.getSerieNumber() - 1, serieResults);
                        currentSerieShotNb++;
                    }
                    // Simple shot result
                } else {
                    points.get(singleResult.getSerieNumber() - 1).set(currentSerieShotNb, singleResult.getPoints());
                    currentSerieShotNb++;
                }
            }
        }
        return new GetParticipationResultsResponse(participationCurrentEntry.getKey(), points);
    }

    private List<Double> initializedSerieResultList(Discipline discipline) {
        List<Double> serieResults = new ArrayList<>(discipline.getNbShotsPerSerie() + 1);
        serieResults.addAll(Collections.nCopies(discipline.getNbShotsPerSerie() + 1, null));
        return serieResults;
    }

    private ShooterResultDto mapResultToDto(ShotResultProjection result) {
        return new ShooterResultDto(
                result.getSerieNumber(),
                result.getShotNumber(),
                result.getPoints()
        );
    }
}
