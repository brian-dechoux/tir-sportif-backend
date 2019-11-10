package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.CreateDisciplineParticipationRequest;
import com.tirsportif.backend.dto.CreateParticipationsRequest;
import com.tirsportif.backend.dto.GetParticipationsResponse;
import com.tirsportif.backend.error.ParticipationError;
import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.exception.ForbiddenException;
import com.tirsportif.backend.exception.NotFoundException;
import com.tirsportif.backend.mapper.ChallengeMapper;
import com.tirsportif.backend.model.*;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ParticipationService extends AbstractService {

    private final ChallengeMapper challengeMapper;
    private final ChallengeRepository challengeRepository;
    private final CategoryRepository categoryRepository;
    private final DisciplineRepository disciplineRepository;
    private final ShooterRepository shooterRepository;
    private final ParticipationRepository participationRepository;
    private final ShotResultRepository shotResultRepository;

    public ParticipationService(ApiProperties apiProperties, ChallengeMapper challengeMapper, ChallengeRepository challengeRepository, CategoryRepository categoryRepository, DisciplineRepository disciplineRepository, ShooterRepository shooterRepository, ParticipationRepository participationRepository, ShotResultRepository shotResultRepository) {
        super(apiProperties);
        this.challengeMapper = challengeMapper;
        this.challengeRepository = challengeRepository;
        this.categoryRepository = categoryRepository;
        this.disciplineRepository = disciplineRepository;
        this.shooterRepository = shooterRepository;
        this.participationRepository = participationRepository;
        this.shotResultRepository = shotResultRepository;
    }

    private Challenge findChallengeById(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new NotFoundException(GenericClientError.RESOURCE_NOT_FOUND, challengeId.toString()));
    }

    private Shooter findShooterById(Long shooterId) {
        return shooterRepository.findById(shooterId)
                .orElseThrow(() -> new NotFoundException(GenericClientError.RESOURCE_NOT_FOUND, shooterId.toString()));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(GenericClientError.RESOURCE_NOT_FOUND, categoryId.toString()));
    }

    private Discipline findDisciplineById(Long disciplineId) {
        return disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new NotFoundException(GenericClientError.RESOURCE_NOT_FOUND, disciplineId.toString()));
    }

    /**
     * Create multiple {@link Participation} instances from a single creation request.
     *
     * @param request, Creation request with challenge, shooter, category, and multiple disciplines
     * @return Generated participations
     */
    public GetParticipationsResponse createParticipations(Long challengeId, CreateParticipationsRequest request) {
        log.info("Creating participations for shooter with ID: {}, for challenge with ID: {}", request.getShooterId(), challengeId);
        Challenge challenge = findChallengeById(challengeId);
        Shooter shooter = findShooterById(request.getShooterId());
        Category category = findCategoryById(request.getCategoryId());

        /*
        TODO Cache categories and disciplines
        Map<Long, Discipline> disciplinesMap =
                findDisciplinesByIds(
                    request.getDisciplinesInformation().stream()
                            .map(CreateDisciplineParticipationRequest::getDisciplineId)
                            .collect(Collectors.toSet())
                ).stream().collect(Collectors.toMap(Discipline::getId, Function.identity()));
        */

        checkAuthorizedDisciplines(challenge, request.getDisciplinesInformation());


        Set<Participation> participations = request.getDisciplinesInformation().stream()
                .map(disciplineInformation -> {
                    Discipline discipline = findDisciplineById(disciplineInformation.getDisciplineId());
                    return Participation.builder()
                            .challenge(challenge)
                            .shooter(shooter)
                            .category(category)
                            .discipline(discipline)
                            .useElectronicTarget(disciplineInformation.isUseElectronicTarget())
                            .paid(disciplineInformation.isPaid())
                            .outrank(disciplineInformation.isOutrank())
                            .build();
                }).collect(Collectors.toSet());

        try {
            participationRepository.saveAll(participations);
        } catch (DataIntegrityViolationException exception) {
            if (exception.getMessage() != null && exception.getMessage().contains(IntegrityConstraints.PARTICIPATION_DISCIPLINE_ONLY_ONE_RANKED.getCauseMessagePart())) {
                throw new ForbiddenException(ParticipationError.PARTICIPATION_DISCIPLINE_RANKED_SHOULD_BE_UNIQUE, challengeId.toString());
            }
            throw exception;
        }

        GetParticipationsResponse response = GetParticipationsResponse.builder()
                .participations(
                        participations.stream()
                                .map(challengeMapper::mapParticipationToResponse)
                                .collect(Collectors.toSet())
                ).build();
        log.info("Participations created");
        return response;
    }

    private void checkAuthorizedDisciplines(Challenge challenge, Set<CreateDisciplineParticipationRequest> requestedDisciplinesInformation) {
        Set<Long> challengeDisciplineIds = challenge.getDisciplines().stream()
                .map(Discipline::getId)
                .collect(Collectors.toSet());
        Set<Long> requestedDisciplineIds = requestedDisciplinesInformation.stream()
                .map(CreateDisciplineParticipationRequest::getDisciplineId)
                .collect(Collectors.toSet());
        if (!challengeDisciplineIds.containsAll(requestedDisciplineIds)) {
            throw new ForbiddenException(ParticipationError.PARTICIPATION_DISCIPLINE_NOT_AUTHORIZED_FOR_CHALLENGE, challenge.getId().toString());
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
            throw new NotFoundException(GenericClientError.RESOURCE_NOT_FOUND, ids);
        }
    }

    private void checkNoExistingShotResultsForParticipation(Long participationId) {
        if (shotResultRepository.existsByParticipationId(participationId)) {
            throw new ForbiddenException(ParticipationError.EXISTING_SHOT_RESULTS_FOR_PARTICIPATION, participationId.toString());
        }
    }

}
