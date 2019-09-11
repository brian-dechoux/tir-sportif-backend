package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.CreateClubRequest;
import com.tirsportif.backend.dto.GetClubResponse;
import com.tirsportif.backend.dto.UpdateClubRequest;
import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.exception.BadRequestException;
import com.tirsportif.backend.exception.NotFoundException;
import com.tirsportif.backend.mapper.AddressMapper;
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
    private final AddressMapper addressMapper;
    private final ClubRepository clubRepository;
    private final CountryStore countryStore;

    public ClubService(ApiProperties apiProperties, ClubMapper clubMapper, AddressMapper addressMapper, ClubRepository clubRepository, CountryStore countryStore) {
        super(apiProperties);
        this.clubMapper = clubMapper;
        this.addressMapper = addressMapper;
        this.clubRepository = clubRepository;
        this.countryStore = countryStore;
    }

    private Country getCountryById(Long id) {
        return countryStore.getCountryById(id)
                .orElseThrow(() -> new BadRequestException(GenericClientError.RESOURCE_NOT_FOUND, id.toString()));
    }

    public void createClub(CreateClubRequest request) {
        log.info("Creating club named: {}", request.getName());
        Country country = getCountryById(request.getAddress().getCountryId());
        Club club = clubMapper.mapCreateClubDtoToClub(request, country);
        clubRepository.save(club);
        log.info("Club created");
    }

    public GetClubResponse getClubById(Long id) {
        log.info("Looking for club with ID: {}", id);
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(GenericClientError.RESOURCE_NOT_FOUND, id.toString()));
        GetClubResponse response = clubMapper.mapClubToResponse(club);
        log.info("Found club");
        return response;
    }

    public Page<GetClubResponse> getClubs(int page) {
        log.info("Looking for all clubs");
        PageRequest pageRequest = PageRequest.of(page, apiProperties.getPaginationSize());
        Page<GetClubResponse> responses = clubRepository.findAll(pageRequest)
                .map(clubMapper::mapClubToResponse);
        log.info("Found {} clubs", responses.getSize());
        return responses;
    }

    public void updateClub(Long id, UpdateClubRequest request) {
        log.info("Updating club named: {}", request.getName());
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(GenericClientError.RESOURCE_NOT_FOUND, id.toString()));
        club.setName(Optional.ofNullable(request.getName()).orElse(club.getName()));
        club.setAddress(
                Optional.ofNullable(request.getAddress())
                        .map(address -> addressMapper.mapAddressDtoToAddress(address, getCountryById(address.getCountryId())))
                        .orElse(club.getAddress())
        );
        clubRepository.save(club);
        log.info("Updated club");
    }

}
