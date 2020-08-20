package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.model.Discipline;
import com.tirsportif.backend.model.ShotResult;
import com.tirsportif.backend.model.key.ShotResultKey;
import com.tirsportif.backend.model.projection.SeriesShotResultForChallengeProjection;
import com.tirsportif.backend.model.projection.ShotResultForCategoryAndDisciplineProjection;
import com.tirsportif.backend.model.projection.ShotResultForChallengeProjection;
import com.tirsportif.backend.model.projection.ShotResultProjection;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @AllArgsConstructor
    @EqualsAndHashCode
    private class TemporaryChallengeCategoryDisciplineMapKey {
        long categoryId;
        String categoryLabel;
        long disciplineId;
        String disciplineLabel;
    }

    /**
     * TODO
     * @param results
     * @return
     */
    public List<GetChallengeCategoryDisciplineResultsResponse> mapChallengeResultsToDto(List<ShotResultForChallengeProjection> results) {
        return results.stream()
                .collect(Collectors.groupingBy(
                        singleResult -> new TemporaryChallengeCategoryDisciplineMapKey(
                                singleResult.getCategoryId(),
                                singleResult.getCategoryLabel(),
                                singleResult.getDisciplineId(),
                                singleResult.getDisciplineLabel()
                        ),
                        LinkedHashMap::new,
                        Collectors.mapping(
                                singleResult -> new GetChallengeCategoryDisciplineResultResponse(
                                        singleResult.getLastname(),
                                        singleResult.getFirstname(),
                                        singleResult.getParticipationId(),
                                        singleResult.getParticipationTotalPoints()
                                ),
                                Collectors.toCollection(LinkedList::new)
                        )
                )).entrySet().stream()
                .map(participationCurrentEntry -> GetChallengeCategoryDisciplineResultsResponse.builder()
                        .categoryId(participationCurrentEntry.getKey().categoryId)
                        .categoryLabel(participationCurrentEntry.getKey().categoryLabel)
                        .disciplineId(participationCurrentEntry.getKey().disciplineId)
                        .disciplineLabel(participationCurrentEntry.getKey().disciplineLabel)
                        .results(
                                participationCurrentEntry.getValue().stream()
                                        .sorted(Comparator.comparingDouble(GetChallengeCategoryDisciplineResultResponse::getParticipationTotalPoints).reversed())
                                        .collect(Collectors.toCollection(LinkedList::new))
                        ).build()
                ).collect(Collectors.toList());
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    private class TemporaryChallengeSeriesMapKey {
        Long shooterId;
        String lastname;
        String firstname;
    }

    /**
     * TODO
     * @param results
     * @return
     */
    public List<GetChallengeSeriesResultsResponse> mapChallengeSeriesResultsToDto(List<SeriesShotResultForChallengeProjection> results) {
        return results.stream()
                .collect(Collectors.groupingBy(
                        singleResult -> new TemporaryChallengeSeriesMapKey(
                                singleResult.getShooterId(),
                                singleResult.getLastname(),
                                singleResult.getFirstname()
                        ),
                        LinkedHashMap::new,
                        Collectors.mapping(
                                SeriesShotResultForChallengeProjection::getPoints,
                                Collectors.toCollection(LinkedList::new)
                        )
                )).entrySet().stream()
                .map(participationCurrentEntry -> GetChallengeSeriesResultsResponse.builder()
                        .lastname(participationCurrentEntry.getKey().lastname)
                        .firstname(participationCurrentEntry.getKey().firstname)
                        .participationSeriesPoints(participationCurrentEntry.getValue())
                        .participationTotalPoints(
                                participationCurrentEntry.getValue().stream()
                                        .mapToDouble(Double::doubleValue)
                                        .sum()
                        ).build()
                ).sorted(Comparator.comparingDouble(GetChallengeSeriesResultsResponse::getParticipationTotalPoints).reversed())
                .collect(Collectors.toList());
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
                        GetParticipationSerieResultsResponse serieResult = serieResults.get(singleResult.getSerieNumber()) != null ?
                                serieResults.get(singleResult.getSerieNumber()) :
                                initializedSerieResultList(discipline);
                        serieResult.setCalculatedTotal(singleResult.getPoints());
                        participationSerieResults = serieResult;
                    }
                    serieResults.set(singleResult.getSerieNumber(), participationSerieResults);
                } else if (singleResult.getShotNumber() == 0) {
                    GetParticipationSerieResultsResponse serieResult = serieResults.get(singleResult.getSerieNumber()) != null ?
                            serieResults.get(singleResult.getSerieNumber()) :
                            initializedSerieResultList(discipline);
                    serieResult.getPoints().set(singleResult.getShotNumber(), singleResult.getPoints());
                    serieResults.set(singleResult.getSerieNumber(), serieResult);
                } else {
                    serieResults.get(singleResult.getSerieNumber()).getPoints().set(singleResult.getShotNumber(), singleResult.getPoints());
                }
            }
        }

        Double participationTotal = serieResults.stream()
                .map(serieResult ->
                    Optional.ofNullable(serieResult.getManualTotal())
                        .or(() -> Optional.ofNullable(serieResult.getCalculatedTotal()))
                        .orElse(0.0)
                ).reduce(Double::sum)
                .orElse(0.0);
        Double roundParticipationTotal = new BigDecimal(participationTotal)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
        return new GetParticipationResultsResponse(participationCurrentEntry.getKey(), serieResults, roundParticipationTotal);
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
