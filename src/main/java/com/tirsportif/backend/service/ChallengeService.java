package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.error.ChallengeError;
import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.exception.BadRequestException;
import com.tirsportif.backend.exception.ForbiddenException;
import com.tirsportif.backend.exception.NotFoundException;
import com.tirsportif.backend.mapper.ChallengeMapper;
import com.tirsportif.backend.model.*;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.*;
import com.tirsportif.backend.utils.IterableUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChallengeService extends AbstractService {

    private final ChallengeMapper challengeMapper;
    private final ChallengeRepository challengeRepository;
    private final ClubRepository clubRepository;
    private final CategoryRepository categoryRepository;
    private final DisciplineRepository disciplineRepository;
    private final ShooterRepository shooterRepository;
    private final ParticipationRepository participationRepository;
    private final CountryStore countryStore;

    public ChallengeService(ApiProperties apiProperties, ChallengeMapper challengeMapper, ChallengeRepository challengeRepository, ClubRepository clubRepository, CategoryRepository categoryRepository, DisciplineRepository disciplineRepository, ShooterRepository shooterRepository, ParticipationRepository participationRepository, CountryStore countryStore) {
        super(apiProperties);
        this.challengeMapper = challengeMapper;
        this.challengeRepository = challengeRepository;
        this.clubRepository = clubRepository;
        this.categoryRepository = categoryRepository;
        this.disciplineRepository = disciplineRepository;
        this.shooterRepository = shooterRepository;
        this.participationRepository = participationRepository;
        this.countryStore = countryStore;
    }

    private Country findCountryById(Long id) {
        return countryStore.getCountryById(id)
                .orElseThrow(() -> new BadRequestException(GenericClientError.RESOURCE_NOT_FOUND, id.toString()));
    }

    private Club findClubById(Long clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new NotFoundException(GenericClientError.RESOURCE_NOT_FOUND, clubId.toString()));
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

    private Set<Category> findCategoriesByIds(Set<Long> categoryIds) {
        Set<Category> categories = IterableUtils.toSet(categoryRepository.findAllById(categoryIds));
        if (categories.size() != categoryIds.size()) {
            throw new NotFoundException(GenericClientError.RESOURCES_NOT_FOUND);
        }
        return categories;
    }

    private Set<Discipline> findDisciplinesByIds(Set<Long> disciplineIds) {
        Set<Discipline> disciplines = IterableUtils.toSet(disciplineRepository.findAllById(disciplineIds));
        if (disciplines.size() != disciplineIds.size()) {
            throw new NotFoundException(GenericClientError.RESOURCES_NOT_FOUND);
        }
        return disciplines;
    }

    public GetChallengeResponse createChallenge(CreateChallengeRequest request) {
        log.info("Creating challenge named : {}", request.getName());
        Country country = findCountryById(request.getAddress().getCountryId());
        Club club = findClubById(request.getOrganiserClubId());
        Set<Category> categories = findCategoriesByIds(request.getCategoryIds());
        Set<Discipline> disciplines = findDisciplinesByIds(request.getDisciplineIds());

        ResolvedCreateChallengeRequest resolvedCreateChallengeRequest = ResolvedCreateChallengeRequest.ofRawRequest(request, country, club, categories, disciplines);
        Challenge challenge = challengeMapper.mapCreateChallengeDtoToChallenge(resolvedCreateChallengeRequest);
        challenge = challengeRepository.save(challenge);

        GetChallengeResponse response = challengeMapper.mapChallengeToResponse(challenge);
        log.info("Challenge created");
        return response;
    }

    public GetChallengeResponse getChallenge(Long challengeId) {
        log.info("Looking for a challenge with ID : {}", challengeId);
        Challenge challenge = findChallengeById(challengeId);
        GetChallengeResponse response = challengeMapper.mapChallengeToResponse(challenge);
        log.info("Found challenge");
        return response;
    }

    public Page<GetChallengeResponse> getChallenges(int page) {
        log.info("Looking for all challenges");
        PageRequest pageRequest = PageRequest.of(page, apiProperties.getPaginationSize());
        Page<GetChallengeResponse> responses = challengeRepository.findAll(pageRequest)
                .map(challengeMapper::mapChallengeToResponse);
        log.info("Found {} clubs", responses.getSize());
        return responses;
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

        participationRepository.saveAll(participations);

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
            throw new ForbiddenException(ChallengeError.PARTICIPATION_DISCIPLINE_NOT_AUTHORIZED_FOR_CHALLENGE, challenge.getId().toString());
        }
    }

    private void checkDisciplineRankedInformation() {
        // TODO Check only one ranked for each discipline (get participation for challenge, shooter, category, discipline and outrank==false)
    }

}
