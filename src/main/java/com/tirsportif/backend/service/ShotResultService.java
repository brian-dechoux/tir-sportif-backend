package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.error.ParticipationError;
import com.tirsportif.backend.error.ShotResultError;
import com.tirsportif.backend.exception.BadRequestErrorException;
import com.tirsportif.backend.mapper.ShotResultMapper;
import com.tirsportif.backend.model.Challenge;
import com.tirsportif.backend.model.Discipline;
import com.tirsportif.backend.model.Participation;
import com.tirsportif.backend.model.ShotResult;
import com.tirsportif.backend.model.projection.ShotResultForCategoryAndDisciplineProjection;
import com.tirsportif.backend.model.projection.ShotResultProjection;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.ChallengeRepository;
import com.tirsportif.backend.repository.DisciplineRepository;
import com.tirsportif.backend.repository.ParticipationRepository;
import com.tirsportif.backend.repository.ShotResultRepository;
import com.tirsportif.backend.utils.RepositoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShotResultService extends AbstractService {

    private final ChallengeRepository challengeRepository;
    private final DisciplineRepository disciplineRepository;
    private final ParticipationRepository participationRepository;
    private final ShotResultRepository shotResultRepository;
    private final ShotResultMapper shotResultMapper;

    public ShotResultService(ApiProperties apiProperties, ChallengeRepository challengeRepository, DisciplineRepository disciplineRepository, ParticipationRepository participationRepository, ShotResultRepository shotResultRepository, ShotResultMapper shotResultMapper) {
        super(apiProperties);
        this.challengeRepository = challengeRepository;
        this.disciplineRepository = disciplineRepository;
        this.participationRepository = participationRepository;
        this.shotResultRepository = shotResultRepository;
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

    private void checkShotResultRequestParameters(Discipline discipline, AddShotResultRequest request) {
        int serieNumber = request.getSerieNumber();
        int expectedNbSeries = discipline.getNbSeries();
        if (request.getSerieNumber() > discipline.getNbSeries()) {
            throw new BadRequestErrorException(ShotResultError.INVALID_SERIE_NUMBER, Integer.toString(serieNumber), Integer.toString(expectedNbSeries));
        }

        int shotNumber = request.getShotNumber();
        int expectedNbShots = discipline.getNbShotsPerSerie();
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
    public void addShotResult(Long challengeId, Long participationId, AddShotResultRequest request) {
        log.info("Adding shot result for challenge: {}, and for participation: {}", challengeId, participationId);
        Challenge challenge = findChallengeById(challengeId);
        Participation participation = findParticipationById(participationId);
        checkParticipationMatchesChallenge(participation, challenge);
        Discipline discipline = participation.getDiscipline();

        checkShotResultRequestParameters(discipline, request);

        ShotResult shotResult = shotResultMapper.mapAddCreateShotResultDtoToShotResult(
                ResolvedAddShotResultRequest.ofRawRequest(request, participation)
        );

        shotResultRepository.save(shotResult);
        log.info("Shot result added.");
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
