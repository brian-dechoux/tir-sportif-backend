package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.AddShotResultRequest;
import com.tirsportif.backend.dto.ResolvedAddShotResultRequest;
import com.tirsportif.backend.error.ParticipationError;
import com.tirsportif.backend.error.ShotResultError;
import com.tirsportif.backend.exception.BadRequestException;
import com.tirsportif.backend.exception.ForbiddenException;
import com.tirsportif.backend.mapper.ShotResultMapper;
import com.tirsportif.backend.model.Challenge;
import com.tirsportif.backend.model.Discipline;
import com.tirsportif.backend.model.Participation;
import com.tirsportif.backend.model.ShotResult;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.ChallengeRepository;
import com.tirsportif.backend.repository.IntegrityConstraints;
import com.tirsportif.backend.repository.ParticipationRepository;
import com.tirsportif.backend.repository.ShotResultRepository;
import com.tirsportif.backend.utils.RepositoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShotResultService extends AbstractService {

    private final ChallengeRepository challengeRepository;
    private final ParticipationRepository participationRepository;
    private final ShotResultRepository shotResultRepository;
    private final ShotResultMapper shotResultMapper;

    public ShotResultService(ApiProperties apiProperties, ChallengeRepository challengeRepository, ParticipationRepository participationRepository, ShotResultRepository shotResultRepository, ShotResultMapper shotResultMapper) {
        super(apiProperties);
        this.challengeRepository = challengeRepository;
        this.participationRepository = participationRepository;
        this.shotResultRepository = shotResultRepository;
        this.shotResultMapper = shotResultMapper;
    }

    private Challenge findChallengeById(Long challengeId) {
        return RepositoryUtils.findById(challengeRepository::findById, challengeId);
    }

    private Participation findParticipationById(Long participationId) {
        return RepositoryUtils.findById(participationRepository::findById, participationId);
    }

    private void checkParticipationMatchesChallenge(Participation participation, Challenge challenge) {
        if (!participation.getChallenge().getId().equals(challenge.getId())) {
            throw new BadRequestException(ParticipationError.PARTICIPATION_DOES_NOT_MATCH_CHALLENGE, participation.getChallenge().getId().toString(), challenge.getId().toString());
        }
    }

    private void checkShotResultRequestParameters(Discipline discipline, AddShotResultRequest request) {
        int serieNumber = request.getSerieNumber();
        int expectedNbSeries = discipline.getNbSeries();
        if (request.getSerieNumber() > discipline.getNbSeries()) {
            throw new BadRequestException(ShotResultError.INVALID_SERIE_NUMBER, Integer.toString(serieNumber), Integer.toString(expectedNbSeries));
        }

        Integer shotNumber = request.getShotNumber();
        int expectedNbShots = discipline.getNbShotsPerSerie();
        if (shotNumber != null && (shotNumber > expectedNbShots)) {
            throw new BadRequestException(ShotResultError.INVALID_SHOT_NUMBER, Integer.toString(shotNumber), Integer.toString(expectedNbShots));
        }

        double points = request.getPoints();
        if (!discipline.isDecimalResult() && ((points % 1) != 0)) {
            throw new BadRequestException(ShotResultError.INVALID_POINTS_VALUE, Double.toString(points));
        }

        double actualPoints = request.getPoints();
        double minPtsValue = discipline.getMinPointsValue();
        double maxPtsValue = discipline.getMaxPointsValue();
        if (minPtsValue > actualPoints || maxPtsValue < actualPoints) {
            throw new BadRequestException(ShotResultError.OUTRANGE_POINTS_VALUE, Double.toString(minPtsValue), Double.toString(maxPtsValue));
        }
    }

    public void addShotResult(Long challengeId, Long participationId, AddShotResultRequest request) {
        log.info("Adding shot result for challenge named: {}, and for participation: {}", challengeId, participationId);
        Challenge challenge = findChallengeById(challengeId);
        Participation participation = findParticipationById(participationId);
        checkParticipationMatchesChallenge(participation, challenge);
        Discipline discipline = participation.getDiscipline();

        checkShotResultRequestParameters(discipline, request);

        ShotResult shotResult = shotResultMapper.mapAddCreateShotResultDtoToShotResult(
                ResolvedAddShotResultRequest.ofRawRequest(request, participation)
        );

        try {
            shotResultRepository.save(shotResult);
        } catch (DataIntegrityViolationException exception) {
            if (exception.getMessage() != null && exception.getMessage().contains(IntegrityConstraints.SHOT_RESULT_UNIQUE.getCauseMessagePart())) {
                throw new ForbiddenException(ShotResultError.SHOT_RESULT_MUST_BE_UNIQUE);
            }
            throw exception;
        }

        log.info("Shot result added.");
    }

}
