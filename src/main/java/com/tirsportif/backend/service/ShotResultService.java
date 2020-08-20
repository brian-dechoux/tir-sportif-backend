package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.error.ParticipationError;
import com.tirsportif.backend.error.ShotResultError;
import com.tirsportif.backend.exception.BadRequestErrorException;
import com.tirsportif.backend.mapper.ChallengeMapper;
import com.tirsportif.backend.mapper.ShotResultMapper;
import com.tirsportif.backend.model.Challenge;
import com.tirsportif.backend.model.Discipline;
import com.tirsportif.backend.model.Participation;
import com.tirsportif.backend.model.ShotResult;
import com.tirsportif.backend.model.key.ShotResultKey;
import com.tirsportif.backend.model.projection.SeriesShotResultForChallengeProjection;
import com.tirsportif.backend.model.projection.ShotResultForCategoryAndDisciplineProjection;
import com.tirsportif.backend.model.projection.ShotResultForChallengeProjection;
import com.tirsportif.backend.model.projection.ShotResultProjection;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.ChallengeRepository;
import com.tirsportif.backend.repository.DisciplineRepository;
import com.tirsportif.backend.repository.ParticipationRepository;
import com.tirsportif.backend.repository.ShotResultRepository;
import com.tirsportif.backend.utils.RepositoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShotResultService extends AbstractService {

    private final ChallengeRepository challengeRepository;
    private final DisciplineRepository disciplineRepository;
    private final ParticipationRepository participationRepository;
    private final ShotResultRepository shotResultRepository;
    private final ChallengeMapper challengeMapper;
    private final ShotResultMapper shotResultMapper;

    public ShotResultService(ApiProperties apiProperties, ChallengeRepository challengeRepository, DisciplineRepository disciplineRepository, ParticipationRepository participationRepository, ShotResultRepository shotResultRepository, ChallengeMapper challengeMapper, ShotResultMapper shotResultMapper) {
        super(apiProperties);
        this.challengeRepository = challengeRepository;
        this.disciplineRepository = disciplineRepository;
        this.participationRepository = participationRepository;
        this.shotResultRepository = shotResultRepository;
        this.challengeMapper = challengeMapper;
        this.shotResultMapper = shotResultMapper;
    }

    private Challenge findChallengeById(Long challengeId) {
        return RepositoryUtils.findById(challengeRepository::findById, challengeId);
    }

    private Discipline findDisciplineById(Long disciplineId) {
        return RepositoryUtils.findById(disciplineRepository::findById, disciplineId);
    }

    private Participation findParticipationById(Long participationId) {
        return RepositoryUtils.findById(participationRepository::findById, participationId);
    }

    private void checkParticipationMatchesChallenge(Participation participation, Challenge challenge) {
        if (!participation.getChallenge().getId().equals(challenge.getId())) {
            throw new BadRequestErrorException(ParticipationError.PARTICIPATION_DOES_NOT_MATCH_CHALLENGE, participation.getChallenge().getId().toString(), challenge.getId().toString());
        }
    }

    private void checkShotResultRequestParameters(Discipline discipline, ResolvedAddShotResultRequest request) {
        int serieNumber = request.getSerieNumber();
        int shotNumber = request.getShotNumber();
        int expectedNbSeries = discipline.getNbSeries();
        int expectedNbShots = discipline.getNbShotsPerSerie();

        if (request.getShotNumber() < -2) {
            throw new BadRequestErrorException(ShotResultError.INVALID_SHOT_NUMBER_OUTBOUNDS);
        }

        if (request.getSerieNumber() > discipline.getNbSeries()) {
            throw new BadRequestErrorException(ShotResultError.INVALID_SERIE_NUMBER, Integer.toString(serieNumber), Integer.toString(expectedNbSeries));
        }

        if (shotNumber >= 0 && (shotNumber > expectedNbShots)) {
            throw new BadRequestErrorException(ShotResultError.INVALID_SHOT_NUMBER, Integer.toString(shotNumber), Integer.toString(expectedNbShots));
        }

        double points = request.getPoints();
        if (!discipline.isDecimalResult() && ((points % 1) != 0)) {
            throw new BadRequestErrorException(ShotResultError.INVALID_POINTS_VALUE);
        }

        double actualPoints = request.getPoints();
        double minPtsValue = discipline.getMinPointsValue();
        double maxPtsValue = discipline.getMaxPointsValue();
        if (request.getShotNumber() >= 0) {
            if (minPtsValue > actualPoints || maxPtsValue < actualPoints) {
                throw new BadRequestErrorException(ShotResultError.OUTRANGE_POINTS_VALUE, Double.toString(minPtsValue), Double.toString(maxPtsValue));
            }
        } else {
            if (minPtsValue > actualPoints || maxPtsValue * discipline.getNbShotsPerSerie() < actualPoints) {
                throw new BadRequestErrorException(ShotResultError.OUTRANGE_TOTAL_POINTS_VALUE, Double.toString(minPtsValue), Double.toString(maxPtsValue * discipline.getNbShotsPerSerie()));
            }
        }
    }

    /**
     * Add shot results.
     *
     * @param challengeId
     * @param participationId
     * @param request
     */
    public GetParticipationResultsResponse addShotResult(Long challengeId, Long participationId, AddShotResultRequest request) {
        log.info("Adding shot result for challenge: {}, and for participation: {}", challengeId, participationId);
        Challenge challenge = findChallengeById(challengeId);
        Participation participation = findParticipationById(participationId);
        checkParticipationMatchesChallenge(participation, challenge);
        Discipline discipline = participation.getDiscipline();

        ResolvedAddShotResultRequest resultRequest = ResolvedAddShotResultRequest.ofRawRequest(request, participation);
        checkShotResultRequestParameters(discipline, resultRequest);

        ShotResult shotResult = shotResultMapper.mapAddCreateShotResultDtoToShotResult(resultRequest);

        shotResultRepository.save(shotResult);
        log.info("Shot result added.");

        double calculatedTotal = shotResultRepository.findAllByParticipationIdAndSerieNumber(shotResult.getParticipation().getId(), shotResult.getId().getSerieNumber()).stream()
                .filter(currentShotResult -> currentShotResult.getId().getShotNumber() >= 0)
                .map(ShotResult::getPoints)
                .reduce(0.0, Double::sum);
        shotResultRepository.save(new ShotResult(
                new ShotResultKey(shotResult.getId().getSerieNumber(), -1, shotResult.getId().getParticipationId()),
                calculatedTotal,
                participation
        ));
        log.info("Shot result serie total recalculated.");

        return getParticipationResults(challengeId, participationId);
    }

    /**
     * Get general results for a challenge.
     * Uses aggregated results with total for each shooter, category and discipline.
     *
     * @param challengeId
     * @return Challenge results
     */
    @Transactional
    public GetChallengeResultsResponse getResultsForChallenge(Long challengeId) {
        log.info("Searching shot results for challenge: {}", challengeId);
        Challenge challenge = findChallengeById(challengeId);
        List<ShotResultForChallengeProjection> results = shotResultRepository.getShotResultsForChallenge(challengeId);
        GetChallengeResultsResponse response = GetChallengeResultsResponse.builder()
                .challenge(challengeMapper.mapChallengeToResponse(challenge))
                .challengeResults(shotResultMapper.mapChallengeResultsToDto(results))
                .build();
        log.info("Found {} results", results.size());
        return response;
    }

    /**
     * Get detailed results for a challenge, a category and a discipline.
     * Uses series results, total is aggregated programatically when building response DTO.
     *
     * @param challengeId
     * @return Challenge results
     */
    @Transactional
    public List<GetChallengeSeriesResultsResponse> getSeriesResultsForChallenge(Long challengeId, Long categoryId, Long disciplineId) {
        log.info("Searching shot results for challenge: {}", challengeId);
        Challenge challenge = findChallengeById(challengeId);
        List<SeriesShotResultForChallengeProjection> results = shotResultRepository.getSeriesShotResultsForChallenge(challengeId, categoryId, disciplineId);
        List<GetChallengeSeriesResultsResponse> responses = shotResultMapper.mapChallengeSeriesResultsToDto(results);
        log.info("Found {} results", results.size());
        return responses;
    }

    /**
     * Get results for a category and a discipline.
     *
     * @param challengeId
     * @param categoryId
     * @param disciplineId
     * @return Category and discipline results
     */
    public GetCategoryAndDisciplineResultsResponse getResultsForCategory(Long challengeId, Long categoryId, Long disciplineId) {
        log.info("Searching shot results for challenge: {}, for category: {} and for discipline: {}", challengeId, categoryId, disciplineId);
        List<ShotResultForCategoryAndDisciplineProjection> results = shotResultRepository.getShotResultsForChallengeAndCategoryAndDiscipline(challengeId, categoryId, disciplineId);
        GetCategoryAndDisciplineResultsResponse response = new GetCategoryAndDisciplineResultsResponse(
                results.stream().map(shotResultMapper::mapCategoryResultToDto).collect(Collectors.toList())
        );
        log.info("Found {} results", results.size());
        return response;
    }

    /**
     * Get results for a shooter and a discipline, all participations included, for a challenge.
     *
     * @param challengeId
     * @param shooterId
     * @param disciplineId
     * @return Shooter results
     */
    public GetShooterResultsResponse getResultsForShooter(Long challengeId, Long shooterId, Long disciplineId) {
        log.info("Searching shot results for challenge: {}, and for category: {}", challengeId, shooterId);
        Discipline discipline = findDisciplineById(disciplineId);
        List<ShotResultProjection> results = shotResultRepository.getShotResultsForChallengeAndShooterAndDiscipline(challengeId, shooterId, disciplineId);
        List<GetParticipationResultsResponse> participationResults = shotResultMapper.mapResultToDto(results, discipline);
        GetShooterResultsResponse response = new GetShooterResultsResponse(participationResults);
        log.info("Found {} results", results.size());
        return response;
    }

    /**
     * Get results for a participation to a challenge.
     *
     * @param challengeId
     * @param participationId
     * @return Shooter results
     */
    public GetParticipationResultsResponse getParticipationResults(Long challengeId, Long participationId) {
        log.info("Searching shot results for participation: {}", participationId);
        Participation participation = findParticipationById(participationId);
        List<ShotResultProjection> results = shotResultRepository.getShotResultsForParticipation(challengeId, participationId);
        GetParticipationResultsResponse response = shotResultMapper.mapResultToDto(results, participation.getDiscipline()).get(0);
        log.info("Found results");
        return response;
    }

}
