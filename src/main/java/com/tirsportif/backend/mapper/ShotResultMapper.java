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
     * TODO rewrite explanation
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
    public List<GetParticipationResultsResponse> mapResultToDto(List<ShotResultProjection> results, Discipline discipline) {
        return results.stream()
                .collect(Collectors.groupingBy(
                        result -> new ParticipationResultReferenceDto(
                                result.getParticipationId(),
                                discipline.getNbShotsPerSerie(),
                                result.getOutrank(),
                                result.getUseElectronicTarget()
                        ),
                        LinkedHashMap::new,
                        Collectors.mapping(this::mapResultToDto, Collectors.toCollection(LinkedList::new))
                )).entrySet().stream()
                .map(participationCurrentEntry -> formatResultsResponse(participationCurrentEntry, discipline))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    // TODO Do best, this is not immutable :-(
    private GetParticipationResultsResponse formatResultsResponse(Map.Entry<ParticipationResultReferenceDto, LinkedList<ShooterResultDto>> participationCurrentEntry, Discipline discipline) {
        // Initialize points with null values in case of no shot results for a serie for example
        List<GetParticipationSerieResultsResponse> serieResults = new ArrayList<>(discipline.getNbSeries());
        for (int i=0 ; i < discipline.getNbSeries() ; i++) {
            serieResults.add(initializedSerieResultList(discipline));
        }

        for (ShooterResultDto singleResult : participationCurrentEntry.getValue()) {
            if (singleResult.getSerieNumber() != null) {
                if (singleResult.getShotNumber() < 0) {
                    GetParticipationSerieResultsResponse participationSerieResults;
                    List<Double> seriePoints = initializedSerieResultList(discipline).getPoints();
                    if (singleResult.getShotNumber() == -2) {
                        participationSerieResults = new GetParticipationSerieResultsResponse(seriePoints, null, singleResult.getPoints());
                    } else {
                        participationSerieResults = new GetParticipationSerieResultsResponse(seriePoints, singleResult.getPoints(), null);
                    }
                    serieResults.set(singleResult.getSerieNumber() - 1, participationSerieResults);
                } else if (singleResult.getShotNumber() == 0) {
                    GetParticipationSerieResultsResponse serieResult = serieResults.get(singleResult.getSerieNumber() - 1) != null ?
                            serieResults.get(singleResult.getSerieNumber() - 1) :
                            initializedSerieResultList(discipline);
                    serieResult.getPoints().set(singleResult.getShotNumber(), singleResult.getPoints());
                    serieResults.set(singleResult.getSerieNumber() - 1, serieResult);
                } else {
                    serieResults.get(singleResult.getSerieNumber() - 1).getPoints().set(singleResult.getShotNumber(), singleResult.getPoints());
                }
            }
        }
        return new GetParticipationResultsResponse(participationCurrentEntry.getKey(), serieResults);
    }

    private GetParticipationSerieResultsResponse initializedSerieResultList(Discipline discipline) {
        List<Double> serieResults = new ArrayList<>(discipline.getNbShotsPerSerie());
        serieResults.addAll(Collections.nCopies(discipline.getNbShotsPerSerie(), null));
        return GetParticipationSerieResultsResponse.init(serieResults);
    }

    private ShooterResultDto mapResultToDto(ShotResultProjection result) {
        return new ShooterResultDto(
                result.getSerieNumber(),
                result.getShotNumber(),
                result.getPoints()
        );
    }
}
