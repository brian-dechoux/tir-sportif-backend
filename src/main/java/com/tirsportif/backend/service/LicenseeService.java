package com.tirsportif.backend.service;

import com.tirsportif.backend.cache.CountryStore;
import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.exception.BadRequestErrorException;
import com.tirsportif.backend.mapper.LicenseeMapper;
import com.tirsportif.backend.model.Club;
import com.tirsportif.backend.model.Country;
import com.tirsportif.backend.model.Licensee;
import com.tirsportif.backend.model.Shooter;
import com.tirsportif.backend.model.event.LicenseSubscriptionEvent;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.property.MyClubProperties;
import com.tirsportif.backend.repository.ClubRepository;
import com.tirsportif.backend.repository.LicenseeRepository;
import com.tirsportif.backend.repository.ShooterRepository;
import com.tirsportif.backend.utils.RepositoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ClubRepository clubRepository;
    private final CountryStore countryStore;
    private final MyClubProperties myClubProperties;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Clock clock;

    public LicenseeService(ApiProperties apiProperties, LicenseeMapper licenseeMapper, LicenseeRepository licenseeRepository, ShooterRepository shooterRepository, ClubRepository clubRepository, CountryStore countryStore, MyClubProperties myClubProperties, ApplicationEventPublisher applicationEventPublisher, Clock clock) {
        super(apiProperties);
        this.licenseeMapper = licenseeMapper;
        this.licenseeRepository = licenseeRepository;
        this.shooterRepository = shooterRepository;
        this.clubRepository = clubRepository;
        this.countryStore = countryStore;
        this.myClubProperties = myClubProperties;
        this.clock = clock;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    private Country findCountryById(Long id) {
        return countryStore.getCountryById(id)
                .orElseThrow(() -> new BadRequestErrorException(GenericClientError.RESOURCE_NOT_FOUND, id.toString()));
    }

    private Licensee findLicenseeById(Long licenseeId) {
        return RepositoryUtils.findById(licenseeRepository::findById, licenseeId);
    }

    private Shooter findShooterById(Long shooterId) {
        return RepositoryUtils.findById(shooterRepository::findById, shooterId);
    }

    private Club findClubById(Long clubId) {
        return RepositoryUtils.findById(clubRepository::findById, clubId);
    }

    /**
     * Create a license for a shooter.
     * The shooter must exists in the system in the first place.
     *
     * The shooter's associated club is changed to myclub if not already the case.
     *
     * @param request
     * @return License created
     */
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

        Club myClub = findClubById(myClubProperties.getId());
        if (shooter.getClub() == null || !shooter.getClub().getId().equals(myClub.getId())) {
            log.info("Updating shooter club");
            shooterRepository.save(shooter.toBuilder().club(myClub).build());
        }

        applicationEventPublisher.publishEvent(new LicenseSubscriptionEvent(licensee));

        GetLicenseeResponse response = licenseeMapper.mapLicenseeToResponse(licensee);
        log.info("Licensee information created");
        return response;
    }

    /**
     * Get paged licensees.
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

    /**
     * Get a specific license.
     *
     * @param id
     * @return License
     */
    public GetLicenseeResponse getLicenseeById(Long id) {
        log.info("Looking for licensee with ID: {}", id);
        Licensee licensee = findLicenseeById(id);
        GetLicenseeResponse response = licenseeMapper.mapLicenseeToResponse(licensee);
        log.info("Found licensee.");
        return response;
    }

    /**
     * Renew a license, starting again from now.
     *
     * @param licenseeId
     * @return Renewed license
     */
    public GetLicenseeResponse renewLicenseeSubscription(Long licenseeId) {
        log.info("Updating licensee subscription date.");
        Licensee licensee = findLicenseeById(licenseeId);
        licensee = licenseeRepository.save(
                licensee.toBuilder()
                    .subscriptionDate(LocalDate.now(clock))
                    .build()
        );
        GetLicenseeResponse response = licenseeMapper.mapLicenseeToResponse(licensee);
        applicationEventPublisher.publishEvent(new LicenseSubscriptionEvent(licensee));
        log.info("Licensee subscription updated.");
        return response;
    }

}
