package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.CreateDisciplineParticipationRequest;
import com.tirsportif.backend.dto.CreateParticipationsRequest;
import com.tirsportif.backend.dto.GetParticipantResponse;
import com.tirsportif.backend.dto.GetShooterParticipationsResponse;
import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.error.ParticipationError;
import com.tirsportif.backend.exception.ForbiddenErrorException;
import com.tirsportif.backend.exception.NotFoundErrorException;
import com.tirsportif.backend.mapper.ParticipationMapper;
import com.tirsportif.backend.model.Challenge;
import com.tirsportif.backend.model.Discipline;
import com.tirsportif.backend.model.Participation;
import com.tirsportif.backend.model.Shooter;
import com.tirsportif.backend.model.event.ParticipationCreated;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class ParticipationService extends AbstractService {

    private final ParticipationMapper participationMapper;
    private final ChallengeRepository challengeRepository;
    private final DisciplineRepository disciplineRepository;
    private final ShooterRepository shooterRepository;
    private final ParticipationRepository participationRepository;
    private final ShotResultRepository shotResultRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ParticipationService(ApiProperties apiProperties, ParticipationMapper participationMapper, ChallengeRepository challengeRepository, DisciplineRepository disciplineRepository, ShooterRepository shooterRepository, ParticipationRepository participationRepository, ShotResultRepository shotResultRepository, ApplicationEventPublisher applicationEventPublisher) {
        super(apiProperties);
        this.participationMapper = participationMapper;
        this.challengeRepository = challengeRepository;
        this.disciplineRepository = disciplineRepository;
        this.shooterRepository = shooterRepository;
        this.participationRepository = participationRepository;
        this.shotResultRepository = shotResultRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    private Challenge findChallengeById(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new NotFoundErrorException(GenericClientError.RESOURCE_NOT_FOUND, challengeId.toString()));
    }

    private Shooter findShooterById(Long shooterId) {
        return shooterRepository.findById(shooterId)
                .orElseThrow(() -> new NotFoundErrorException(GenericClientError.RESOURCE_NOT_FOUND, shooterId.toString()));
    }

    private Discipline findDisciplineById(Long disciplineId) {
        return disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new NotFoundErrorException(GenericClientError.RESOURCE_NOT_FOUND, disciplineId.toString()));
    }

    /**
     * Get all participants (distinct shooters from all participations) for a specific challenge.
     *
     * @param challengeId Challenge ID
     * @param page Page number
     * @return Paginated shooters
     */
    public Page<GetParticipantResponse> getParticipants(Long challengeId, int page, int rowsPerPage) {
        log.info("Looking for all participants to challenge with ID: {}", challengeId);
        findChallengeById(challengeId);
        PageRequest pageRequest = PageRequest.of(page, rowsPerPage);
        Page<GetParticipantResponse> responses = participationRepository.findParticipantsByChallengeId(challengeId, pageRequest)
                .map(participationMapper::mapParticipantToResponse);
        log.info("Found {} participants to challenge {}", responses.getNumberOfElements(), challengeId);
        return responses;
    }

    /**
     * Get all participations for a participant (shooter) at a specific challenge.
     *
     * @param challengeId Challenge ID
     * @param participantId Participant ID
     */
    public GetShooterParticipationsResponse getParticipations(Long challengeId, Long participantId) {
        log.info("Looking for all participations to challenge with ID: {} for a shooter with ID: {}", challengeId, participantId);
        findChallengeById(challengeId);
        Shooter shooter = findShooterById(participantId);
        List<Participation> participations = participationRepository.findByChallengeIdAndShooterId(challengeId, participantId);
        GetShooterParticipationsResponse response = participationMapper.mapShooterAndParticipationsToResponse(shooter, participations);
        log.info("Found {} participations", response.getParticipations().size());
        return response;
    }

    /**
     * Create multiple {@link Participation} instances from a single creation request.
     *
     * @param request, Creation request with challenge, shooter, category, and multiple disciplines
     * @return Generated participations
     */
    @Transactional
    public GetShooterParticipationsResponse createParticipations(Long challengeId, CreateParticipationsRequest request) {
        log.info("Creating participations for shooter with ID: {}, for challenge with ID: {}", request.getShooterId(), challengeId);
        Challenge challenge = findChallengeById(challengeId);
        Shooter shooter = findShooterById(request.getShooterId());

        /*
        TODO Cache categories and disciplines
        Map<Long, Discipline> disciplinesMap =
                findDisciplinesByIds(
                    request.getDisciplinesInformation().stream()
                            .map(CreateDisciplineParticipationRequest::getDisciplineId)
                            .collect(Collectors.toSet())
                ).stream().collect(Collectors.toMap(Discipline::getId, Function.identity()));
        */

        // TODO Check also categories...
        checkAuthorizedDisciplines(challenge, request.getDisciplinesInformation());

        List<Participation> participations = request.getDisciplinesInformation().stream()
                .map(disciplineInformation -> {
                    Discipline discipline = findDisciplineById(disciplineInformation.getDisciplineId());
                    return Participation.builder()
                            .challenge(challenge)
                            .shooter(shooter)
                            .discipline(discipline)
                            .paid(disciplineInformation.isPaid())
                            .useElectronicTarget(disciplineInformation.isUseElectronicTarget())
                            .outrank(disciplineInformation.isOutrank())
                            .build();
                }).collect(Collectors.toList());

        try {
            participations = StreamSupport.stream(participationRepository.saveAll(participations).spliterator(), true).collect(Collectors.toList());
            log.info("Participations created");
        } catch (DataIntegrityViolationException exception) {
            if (exception.getMessage() != null && exception.getMessage().contains(IntegrityConstraints.PARTICIPATION_DISCIPLINE_ONLY_ONE_RANKED.getCauseMessagePart())) {
                throw new ForbiddenErrorException(ParticipationError.PARTICIPATION_DISCIPLINE_RANKED_SHOULD_BE_UNIQUE, challengeId.toString());
            }
            throw exception;
        }

        log.info("Sending participations to billing service");
        participations.stream()
                .map(ParticipationCreated::new)
                .forEach(applicationEventPublisher::publishEvent);

        participations = participationRepository.findByChallengeIdAndShooterId(challengeId, shooter.getId());

        log.info("Mapping results");
        return participationMapper.mapShooterAndParticipationsToResponse(shooter, participations);
    }

    private void checkAuthorizedDisciplines(Challenge challenge, Set<CreateDisciplineParticipationRequest> requestedDisciplinesInformation) {
        Set<Long> challengeDisciplineIds = challenge.getDisciplines().stream()
                .map(Discipline::getId)
                .collect(Collectors.toSet());
        Set<Long> requestedDisciplineIds = requestedDisciplinesInformation.stream()
                .map(CreateDisciplineParticipationRequest::getDisciplineId)
                .collect(Collectors.toSet());
        //if (!challengeDisciplineIds.containsAll(requestedDisciplineIds)) {
        if (!challengeDisciplineIds.containsAll(requestedDisciplineIds)) {
            throw new ForbiddenErrorException(ParticipationError.PARTICIPATION_DISCIPLINE_NOT_AUTHORIZED_FOR_CHALLENGE, challenge.getId().toString());
        }
    }

    private void checkDisciplineRankedInformation() {
        // TODO Check only one ranked for each discipline (get participation for challenge, shooter, category, discipline and outrank==false)
    }

    /**
     * Delete a participation for a challenge.
     *
     * @param challengeId       Challenge
     * @param participationId   Participation
     */
    public void deleteParticipation(Long challengeId, Long participationId) {
        log.info("Deleting participation with ID: {}, for challenge with ID: {}", participationId, challengeId);
        checkExistingParticipationForChallenge(participationId, challengeId);
        checkNoExistingShotResultsForParticipation(participationId);
        participationRepository.deleteById(participationId);
        log.info("Participation deleted");
    }

    private void checkExistingParticipationForChallenge(Long participationId, Long challengeId) {
        if (!participationRepository.existsByChallengeIdAndId(challengeId, participationId)) {
            String ids = "ChallengeId: "+challengeId.toString()+", Id: "+participationId.toString();
            throw new NotFoundErrorException(GenericClientError.RESOURCE_NOT_FOUND, ids);
        }
    }

    private void checkNoExistingShotResultsForParticipation(Long participationId) {
        if (shotResultRepository.existsByParticipationId(participationId)) {
            throw new ForbiddenErrorException(ParticipationError.EXISTING_SHOT_RESULTS_FOR_PARTICIPATION, participationId.toString());
        }
    }

}
