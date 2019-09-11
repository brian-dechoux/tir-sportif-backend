package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.CreateShooterRequest;
import com.tirsportif.backend.dto.GetShooterResponse;
import com.tirsportif.backend.dto.ResolvedCreateShooterRequest;
import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.exception.BadRequestException;
import com.tirsportif.backend.exception.NotFoundException;
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

    private Country getCountryById(Long id) {
        return countryStore.getCountryById(id)
                .orElseThrow(() -> new BadRequestException(GenericClientError.RESOURCE_NOT_FOUND, id.toString()));
    }

    private Club findClubById(Long clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new NotFoundException(GenericClientError.RESOURCE_NOT_FOUND, clubId.toString()));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(GenericClientError.RESOURCE_NOT_FOUND, categoryId.toString()));
    }

    public void createShooter(CreateShooterRequest request) {
        log.info("Creating shooter named : {} {}", request.getLastname(), request.getFirstname());
        Club club = findClubById(request.getClubId());
        Category category = findCategoryById(request.getCategoryId());
        Shooter shooter = shooterMapper.mapCreateShooterDtoToShooter(
                ResolvedCreateShooterRequest.ofRawRequest(request, club, category),
                getCountryById(request.getAddress().getCountryId())
        );
        shooterRepository.save(shooter);
        log.info("Shooter created");
    }

    public GetShooterResponse getShooterById(Long id) {
        log.info("Looking for shooter with ID: {}", id);
        Shooter shooter = shooterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(GenericClientError.RESOURCE_NOT_FOUND, id.toString()));
        GetShooterResponse response = shooterMapper.mapShooterToResponse(shooter);
        log.info("Found shooter");
        return response;
    }

}