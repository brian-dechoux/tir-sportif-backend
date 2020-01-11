package com.tirsportif.backend.service;

import com.tirsportif.backend.cache.CountryStore;
import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.exception.BadRequestErrorException;
import com.tirsportif.backend.exception.NotFoundErrorException;
import com.tirsportif.backend.mapper.ClubMapper;
import com.tirsportif.backend.model.Club;
import com.tirsportif.backend.model.Country;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.ClubRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ClubService extends AbstractService {

    private final ClubMapper clubMapper;
    private final ClubRepository clubRepository;
    private final CountryStore countryStore;

    public ClubService(ApiProperties apiProperties, ClubMapper clubMapper, ClubRepository clubRepository, CountryStore countryStore) {
        super(apiProperties);
        this.clubMapper = clubMapper;
        this.clubRepository = clubRepository;
        this.countryStore = countryStore;
    }

    private Country findCountryById(Long id) {
        return countryStore.getCountryById(id)
                .orElseThrow(() -> new BadRequestErrorException(GenericClientError.RESOURCE_NOT_FOUND, id.toString()));
    }

    public GetClubResponse createClub(CreateClubRequest request) {
        log.info("Creating club named: {}", request.getName());
        Country country = findCountryById(request.getAddress().getCountryId());
        ResolvedCreateClubRequest resolvedCreateAddressRequest = ResolvedCreateClubRequest.ofRawRequest(request, country);
        Club club = clubMapper.mapCreateClubDtoToClub(resolvedCreateAddressRequest);
        club = clubRepository.save(club);

        GetClubResponse response = clubMapper.mapClubToResponse(club);
        log.info("Club created");
        return response;
    }

    public GetClubResponse getClubById(Long id) {
        log.info("Looking for club with ID: {}", id);
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new NotFoundErrorException(GenericClientError.RESOURCE_NOT_FOUND, id.toString()));
        GetClubResponse response = clubMapper.mapClubToResponse(club);
        log.info("Found club");
        return response;
    }

    public Page<GetClubResponse> getClubs(int page) {
        log.info("Looking for all clubs");
        PageRequest pageRequest = PageRequest.of(page, apiProperties.getPaginationSize());
        Page<GetClubResponse> responses = clubRepository.findAll(pageRequest)
                .map(clubMapper::mapClubToResponse);
        log.info("Found {} clubs", responses.getNumber());
        return responses;
    }

    // TODO See ChallengeService.update, better way of handling
    public GetClubResponse updateClub(Long id, UpdateClubRequest request) {
        log.info("Updating club named: {}", request.getName());
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new NotFoundErrorException(GenericClientError.RESOURCE_NOT_FOUND, id.toString()));
        Country country = Optional.ofNullable(request.getAddress())
                .map(CreateAddressRequest::getCountryId)
                .map(this::findCountryById)
                .orElse(null);
        ResolvedUpdateClubRequest resolvedUpdateClubRequest = ResolvedUpdateClubRequest.ofRawRequest(request, country);
        Club updated = clubMapper.mapUpdateClubDtoToClub(club, resolvedUpdateClubRequest);
        updated = clubRepository.save(updated);

        GetClubResponse response = clubMapper.mapClubToResponse(updated);
        log.info("Club updated");
        return response;
    }

}
