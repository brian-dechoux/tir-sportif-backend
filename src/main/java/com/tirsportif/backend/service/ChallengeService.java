package com.tirsportif.backend.service;

import com.tirsportif.backend.cache.CountryStore;
import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.mapper.ChallengeMapper;
import com.tirsportif.backend.model.*;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.CategoryRepository;
import com.tirsportif.backend.repository.ChallengeRepository;
import com.tirsportif.backend.repository.ClubRepository;
import com.tirsportif.backend.repository.DisciplineRepository;
import com.tirsportif.backend.utils.RepositoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ChallengeService extends AbstractService {

    private final ChallengeMapper challengeMapper;
    private final ChallengeRepository challengeRepository;
    private final ClubRepository clubRepository;
    private final CategoryRepository categoryRepository;
    private final DisciplineRepository disciplineRepository;
    private final CountryStore countryStore;

    public ChallengeService(ApiProperties apiProperties, ChallengeMapper challengeMapper, ChallengeRepository challengeRepository, ClubRepository clubRepository, CategoryRepository categoryRepository, DisciplineRepository disciplineRepository, CountryStore countryStore) {
        super(apiProperties);
        this.challengeMapper = challengeMapper;
        this.challengeRepository = challengeRepository;
        this.clubRepository = clubRepository;
        this.categoryRepository = categoryRepository;
        this.disciplineRepository = disciplineRepository;
        this.countryStore = countryStore;
    }

    private Country findCountryById(Long id) {
        return RepositoryUtils.findById(countryStore::getCountryById, id);
    }

    private Club findClubById(Long clubId) {
        return RepositoryUtils.findById(clubRepository::findById, clubId);
    }

    private Challenge findChallengeById(Long challengeId) {
        return RepositoryUtils.findById(challengeRepository::findById, challengeId);
    }

    private Set<Category> findCategoriesByIds(Set<Long> categoryIds) {
        return RepositoryUtils.findByIds(categoryRepository::findAllById, categoryIds);
    }

    private Set<Discipline> findDisciplinesByIds(Set<Long> disciplineIds) {
        return RepositoryUtils.findByIds(disciplineRepository::findAllById, disciplineIds);
    }

    /**
     * Create an empty challenge.
     *
     * @param request
     * @return Created challenge
     */
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

    /**
     * Get a single challenge.
     *
     * @param challengeId
     * @return Challenge's information
     */
    @Transactional
    public GetChallengeResponse getChallenge(Long challengeId) {
        log.info("Looking for a challenge with ID : {}", challengeId);
        Challenge challenge = findChallengeById(challengeId);
        GetChallengeResponse response = challengeMapper.mapChallengeToResponse(challenge);
        log.info("Found challenge");
        return response;
    }

    /**
     * Get all challenges.
     *
     * @param page Page number
     * @return Paginated challenges
     */
    public Page<GetChallengeListElementResponse> getChallenges(int page, int rowsPerPage) {
        log.info("Looking for all challenges");
        PageRequest pageRequest = PageRequest.of(page, rowsPerPage);
        Page<GetChallengeListElementResponse> responses = challengeRepository.findAllAsListElements(pageRequest)
                .map(challengeMapper::mapChallengeListElementToResponse);
        log.info("Found {} participations", responses.getNumberOfElements());
        return responses;
    }

    /**
     * Update an existing challenge's information.
     *
     * @param request
     * @return Updated challenge
     */
    public GetChallengeResponse updateChallenge(Long challengeId, UpdateChallengeRequest request) {
        log.info("Updating challenge named : {}", request.getName());
        Challenge challenge = findChallengeById(challengeId);
        Optional<Country> optCountry = Optional.ofNullable(request.getAddress())
                .map(CreateAddressRequest::getCountryId)
                .flatMap(countryStore::getCountryById);
        Optional<Club> optClub = Optional.ofNullable(request.getOrganiserClubId())
                .flatMap(clubRepository::findById);
        Optional<Set<Category>> optCategories = Optional.ofNullable(request.getCategoryIds())
                .map(this::findCategoriesByIds);
        Optional<Set<Discipline>> optDisciplines = Optional.ofNullable(request.getDisciplineIds())
                .map(this::findDisciplinesByIds);

        ResolvedUpdateChallengeRequest resolvedUpdateChallengeRequest = ResolvedUpdateChallengeRequest.ofRawRequest(request, optCountry, optClub, optCategories, optDisciplines);
        Challenge updated = challengeMapper.mapUpdateChallengeDtoToChallenge(challenge, resolvedUpdateChallengeRequest);
        updated = challengeRepository.save(updated);

        GetChallengeResponse response = challengeMapper.mapChallengeToResponse(updated);
        log.info("Challenge updated");
        return response;
    }

}
