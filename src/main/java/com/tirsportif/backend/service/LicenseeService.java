package com.tirsportif.backend.service;

import com.tirsportif.backend.cache.CountryStore;
import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.exception.BadRequestErrorException;
import com.tirsportif.backend.exception.NotFoundErrorException;
import com.tirsportif.backend.mapper.LicenseeMapper;
import com.tirsportif.backend.model.Country;
import com.tirsportif.backend.model.Licensee;
import com.tirsportif.backend.model.Shooter;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.LicenseeRepository;
import com.tirsportif.backend.repository.ShooterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
public class LicenseeService extends AbstractService {

    private final LicenseeMapper licenseeMapper;
    private final LicenseeRepository licenseeRepository;
    private final ShooterRepository shooterRepository;
    private final CountryStore countryStore;
    private final Clock clock;

    public LicenseeService(ApiProperties apiProperties, LicenseeMapper licenseeMapper, LicenseeRepository licenseeRepository, ShooterRepository shooterRepository, CountryStore countryStore, Clock clock) {
        super(apiProperties);
        this.licenseeMapper = licenseeMapper;
        this.licenseeRepository = licenseeRepository;
        this.shooterRepository = shooterRepository;
        this.countryStore = countryStore;
        this.clock = clock;
    }

    private Country findCountryById(Long id) {
        return countryStore.getCountryById(id)
                .orElseThrow(() -> new BadRequestErrorException(GenericClientError.RESOURCE_NOT_FOUND, id.toString()));
    }

    private Licensee findLicenseeById(Long licenseeId) {
        return licenseeRepository.findById(licenseeId)
                .orElseThrow(() -> new NotFoundErrorException(GenericClientError.RESOURCE_NOT_FOUND, licenseeId.toString()));
    }

    private Shooter findShooterById(Long shooterId) {
        return shooterRepository.findById(shooterId)
                .orElseThrow(() -> new NotFoundErrorException(GenericClientError.RESOURCE_NOT_FOUND, shooterId.toString()));
    }

    public GetLicenseeResponse createLicensee(CreateLicenseeRequest request) {
        log.info("Creating licensee");
        Country country = Optional.ofNullable(request.getAddress())
                .map(CreateAddressRequest::getCountryId)
                .map(this::findCountryById)
                .orElse(null);
        Shooter shooter = findShooterById(request.getShooterId());
        Licensee licensee = licenseeMapper.mapCreateLicenseeDtoToLicensee(
                ResolvedCreateLicenseeRequest.ofRawRequest(request, shooter, country),
                LocalDate.now(clock)
        );
        licensee = licenseeRepository.save(licensee);
        GetLicenseeResponse response = licenseeMapper.mapLicenseeToResponse(licensee);
        log.info("Licensee information created. Should still need to be associated to a shooter.");
        return response;
    }

    /**
     * Get all licensees.
     *
     * @param page Page number
     * @return Paginated licensees
     */
    public Page<GetLicenseeListElementResponse> getLicensees(int page, int rowsPerPage) {
        log.info("Looking for all licensees");
        PageRequest pageRequest = PageRequest.of(page, rowsPerPage);
        Page<GetLicenseeListElementResponse> responses = licenseeRepository.findAllAsListElements(pageRequest)
                .map(licenseeMapper::mapLicenseeListElementToResponse);
        log.info("Found {} licensees", responses.getNumberOfElements());
        return responses;
    }

    public GetLicenseeResponse getLicenseeById(Long id) {
        log.info("Looking for licensee with ID: {}", id);
        Licensee licensee = findLicenseeById(id);
        GetLicenseeResponse response = licenseeMapper.mapLicenseeToResponse(licensee);
        log.info("Found licensee.");
        return response;
    }

    public GetLicenseeResponse updateLicenseeSubscription(Long licenseeId) {
        log.info("Updating licensee subscription date.");
        Licensee licensee = findLicenseeById(licenseeId);
        licensee = licenseeRepository.save(
                licensee.toBuilder()
                    .subscriptionDate(LocalDate.now(clock))
                    .build()
        );
        GetLicenseeResponse response = licenseeMapper.mapLicenseeToResponse(licensee);
        log.info("Licensee subscription updated.");
        return response;
    }

    public GetLicenseeResponse associateLicenseeToShooter(Long licenseeId, Long shooterId) {
        log.info("Associating licensee with ID: {} to the shooter with ID: {}", licenseeId, shooterId);
        Licensee licensee = findLicenseeById(licenseeId);
        Shooter shooter = findShooterById(shooterId);
        licensee = licenseeRepository.save(
                licensee.toBuilder()
                        .shooter(shooter)
                        .build()
        );
        GetLicenseeResponse response = licenseeMapper.mapLicenseeToResponse(licensee);
        log.info("Licensee associated.");
        return response;
    }

}
