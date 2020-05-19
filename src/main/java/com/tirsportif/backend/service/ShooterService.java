package com.tirsportif.backend.service;

import com.tirsportif.backend.cache.CountryStore;
import com.tirsportif.backend.dto.CreateAddressRequest;
import com.tirsportif.backend.dto.CreateShooterRequest;
import com.tirsportif.backend.dto.GetShooterResponse;
import com.tirsportif.backend.dto.ResolvedCreateShooterRequest;
import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.exception.BadRequestErrorException;
import com.tirsportif.backend.exception.NotFoundErrorException;
import com.tirsportif.backend.mapper.ShooterMapper;
import com.tirsportif.backend.model.Category;
import com.tirsportif.backend.model.Club;
import com.tirsportif.backend.model.Country;
import com.tirsportif.backend.model.Shooter;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.CategoryRepository;
import com.tirsportif.backend.repository.ClubRepository;
import com.tirsportif.backend.repository.ShooterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ShooterService extends AbstractService {

    private final ShooterMapper shooterMapper;
    private final ShooterRepository shooterRepository;
    private final ClubRepository clubRepository;
    private final CategoryRepository categoryRepository;
    private final CountryStore countryStore;

    public ShooterService(ApiProperties apiProperties, ShooterMapper shooterMapper, ShooterRepository shooterRepository, ClubRepository clubRepository, CategoryRepository categoryRepository, CountryStore countryStore) {
        super(apiProperties);
        this.shooterMapper = shooterMapper;
        this.shooterRepository = shooterRepository;
        this.clubRepository = clubRepository;
        this.categoryRepository = categoryRepository;
        this.countryStore = countryStore;
    }

    private Country findCountryById(Long id) {
        return countryStore.getCountryById(id)
                .orElseThrow(() -> new BadRequestErrorException(GenericClientError.RESOURCE_NOT_FOUND, id.toString()));
    }

    private Shooter findShooterById(Long shooterId) {
        return shooterRepository.findById(shooterId)
                .orElseThrow(() -> new NotFoundErrorException(GenericClientError.RESOURCE_NOT_FOUND, shooterId.toString()));
    }

    private Club findClubById(Long clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new NotFoundErrorException(GenericClientError.RESOURCE_NOT_FOUND, clubId.toString()));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundErrorException(GenericClientError.RESOURCE_NOT_FOUND, categoryId.toString()));
    }

    public GetShooterResponse createShooter(CreateShooterRequest request) {
        log.info("Creating shooter named : {} {}", request.getLastname(), request.getFirstname());
        Country country = Optional.ofNullable(request.getAddress())
                .map(CreateAddressRequest::getCountryId)
                .map(this::findCountryById)
                .orElse(null);
        Club club = Optional.ofNullable(request.getClubId())
                .map(this::findClubById)
                .orElse(null);
        Category category = findCategoryById(request.getCategoryId());
        Shooter shooter = shooterMapper.mapCreateShooterDtoToShooter(
                ResolvedCreateShooterRequest.ofRawRequest(request, country, club, category)
        );
        shooter = shooterRepository.save(shooter);
        GetShooterResponse response = shooterMapper.mapShooterToResponse(shooter);
        log.info("Shooter created");
        return response;
    }

    public GetShooterResponse getShooterById(Long id) {
        log.info("Looking for shooter with ID: {}", id);
        Shooter shooter = shooterRepository.findById(id)
                .orElseThrow(() -> new NotFoundErrorException(GenericClientError.RESOURCE_NOT_FOUND, id.toString()));
        GetShooterResponse response = shooterMapper.mapShooterToResponse(shooter);
        log.info("Found shooter");
        return response;
    }

    public GetShooterResponse associateShooter(Long shooterId, Long clubId) {
        log.info("Associating shooter with ID: {} to the club with ID: {}", shooterId, clubId);
        Shooter shooter = findShooterById(shooterId);
        Club club = findClubById(clubId);
        shooter = shooterRepository.save(
                shooter.toBuilder()
                    .club(club)
                    .build()
        );
        GetShooterResponse response = shooterMapper.mapShooterToResponse(shooter);
        log.info("Shooter associated.");
        return response;
    }

}
