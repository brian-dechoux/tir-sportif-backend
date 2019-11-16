package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.CreateChallengeRequest;
import com.tirsportif.backend.dto.GetChallengeResponse;
import com.tirsportif.backend.dto.ResolvedCreateChallengeRequest;
import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.exception.BadRequestException;
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

@Service
@Slf4j
public class ChallengeService extends AbstractService {

    private final ChallengeMapper challengeMapper;
    private final ChallengeRepository challengeRepository;
    private final ClubRepository clubRepository;
    private final CategoryRepository categoryRepository;
    private final DisciplineRepository disciplineRepository;
    private final ShooterRepository shooterRepository;
    private final CountryStore countryStore;

    public ChallengeService(ApiProperties apiProperties, ChallengeMapper challengeMapper, ChallengeRepository challengeRepository, ClubRepository clubRepository, CategoryRepository categoryRepository, DisciplineRepository disciplineRepository, ShooterRepository shooterRepository, CountryStore countryStore) {
        super(apiProperties);
        this.challengeMapper = challengeMapper;
        this.challengeRepository = challengeRepository;
        this.clubRepository = clubRepository;
        this.categoryRepository = categoryRepository;
        this.disciplineRepository = disciplineRepository;
        this.shooterRepository = shooterRepository;
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

}