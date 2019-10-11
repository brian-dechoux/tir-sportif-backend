package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.CreateLicenseeRequest;
import com.tirsportif.backend.dto.GetLicenseeResponse;
import com.tirsportif.backend.dto.ResolvedCreateLicenseeRequest;
import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.exception.NotFoundException;
import com.tirsportif.backend.mapper.LicenseeMapper;
import com.tirsportif.backend.model.Licensee;
import com.tirsportif.backend.model.Shooter;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.LicenseeRepository;
import com.tirsportif.backend.repository.ShooterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;

@Service
@Slf4j
public class LicenseeService extends AbstractService {

    private final LicenseeMapper licenseeMapper;
    private final LicenseeRepository licenseeRepository;
    private final ShooterRepository shooterRepository;
    private final Clock clock;

    public LicenseeService(ApiProperties apiProperties, LicenseeMapper licenseeMapper, LicenseeRepository licenseeRepository, ShooterRepository shooterRepository, Clock clock) {
        super(apiProperties);
        this.licenseeMapper = licenseeMapper;
        this.licenseeRepository = licenseeRepository;
        this.shooterRepository = shooterRepository;
        this.clock = clock;
    }

    private Licensee findLicenseeById(Long licenseeId) {
        return licenseeRepository.findById(licenseeId)
                .orElseThrow(() -> new NotFoundException(GenericClientError.RESOURCE_NOT_FOUND, licenseeId.toString()));
    }

    private Shooter findShooterById(Long shooterId) {
        return shooterRepository.findById(shooterId)
                .orElseThrow(() -> new NotFoundException(GenericClientError.RESOURCE_NOT_FOUND, shooterId.toString()));
    }

    public GetLicenseeResponse createLicensee(CreateLicenseeRequest request) {
        log.info("Creating licensee");
        Shooter shooter = findShooterById(request.getShooterId());
        Licensee licensee = licenseeMapper.mapCreateLicenseeDtoToLicensee(
                ResolvedCreateLicenseeRequest.ofRawRequest(request, shooter),
                OffsetDateTime.now(clock)
        );
        licensee = licenseeRepository.save(licensee);
        GetLicenseeResponse response = licenseeMapper.mapLicenseeToResponse(licensee);
        log.info("Licensee information created. Should still need to be associated to a shooter.");
        return response;
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
                    .subscriptionDate(OffsetDateTime.now(clock))
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
