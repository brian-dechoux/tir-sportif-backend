package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.GetChallengeFinanceResponse;
import com.tirsportif.backend.dto.GetShooterFinanceResponse;
import com.tirsportif.backend.dto.GetShooterWithFinancesListElementResponse;
import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.exception.NotFoundErrorException;
import com.tirsportif.backend.mapper.BillMapper;
import com.tirsportif.backend.model.Shooter;
import com.tirsportif.backend.model.event.BillPaidEvent;
import com.tirsportif.backend.model.projection.ShooterBillProjection;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.BillRepository;
import com.tirsportif.backend.repository.ShooterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FinanceService extends AbstractService {

    private final ShooterRepository shooterRepository;
    private final BillRepository billRepository;
    private final BillMapper billMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public FinanceService(ApiProperties apiProperties, ShooterRepository shooterRepository, BillRepository billRepository, BillMapper billMapper, ApplicationEventPublisher applicationEventPublisher) {
        super(apiProperties);
        this.shooterRepository = shooterRepository;
        this.billRepository = billRepository;
        this.billMapper = billMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Get all shooter bills for a specific challenge.
     *
     * @param challengeId
     * @return bills
     */
    public GetChallengeFinanceResponse getChallengeFinances(Long challengeId) {
        log.info("Searching all bills for challenge with ID: {}", challengeId);

        /*List<Bill> bills = billRepository.getBillsForChallenge(challengeId);
        List<GetShooterBillResponse> billResponses = bills.stream()
                .map(billMapper::mapShooterBillToResponse)
                .collect(Collectors.toList());
        GetChallengeFinanceResponse response = new GetChallengeFinanceResponse(billResponses);
        log.info("Found {} bills", bills.size());*/

        return null;
    }

    /**
     * Get all shooters with finances.
     *
     * @param page Page number
     * @return Paginated challenges
     */
    public Page<GetShooterWithFinancesListElementResponse> getShootersWithFinances(int page, int rowsPerPage) {
        log.info("Looking for all challenges");
        PageRequest pageRequest = PageRequest.of(page, rowsPerPage);
        Page<GetShooterWithFinancesListElementResponse> responses = billRepository.findShootersWithBillsAsListElements(pageRequest)
                .map(billMapper::mapShooterWithBillsProjectionToResponse);
        log.info("Found {} shooters", responses.getNumberOfElements());
        return responses;
    }

    /**
     * Get all bills for a specific shooter.
     *
     * @param shooterId
     * @return bills
     */
    public GetShooterFinanceResponse getShooterFinances(Long shooterId) {
        log.info("Searching all bills for shooter with ID: {}", shooterId);

        List<ShooterBillProjection> bills = billRepository.getBillsForShooter(shooterId);
        Shooter shooter = shooterRepository.findById(shooterId)
                .orElseThrow(() -> new NotFoundErrorException(GenericClientError.RESOURCE_NOT_FOUND, shooterId.toString()));
        GetShooterFinanceResponse finances = billMapper.mapShooterBillsToResponse(shooter, bills);

        log.info("Found {} bills", bills.size());
        return finances;
    }

    /**
     * Pay bill with ID.
     *
     * @param billId
     */
    public void payBill(Long billId) {
        log.info("Paying bill with ID: {}", billId);
        applicationEventPublisher.publishEvent(new BillPaidEvent(billId));
        log.info("Bill paid");
    }

}
