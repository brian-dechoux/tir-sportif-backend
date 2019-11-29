package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.GetBillResponse;
import com.tirsportif.backend.dto.GetChallengeFinanceResponse;
import com.tirsportif.backend.dto.GetShooterFinanceResponse;
import com.tirsportif.backend.mapper.BillMapper;
import com.tirsportif.backend.model.Bill;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.BillRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FinanceService extends AbstractService {

    private final BillRepository billRepository;
    private final BillMapper billMapper;

    public FinanceService(ApiProperties apiProperties, BillRepository billRepository, BillMapper billMapper) {
        super(apiProperties);
        this.billRepository = billRepository;
        this.billMapper = billMapper;
    }

    // TODO TO TEST

    /**
     * Get all shooter bills for a specific challenge.
     *
     * @param challengeId
     * @return bills
     */
    public GetChallengeFinanceResponse getChallengeFinances(Long challengeId) {
        log.info("Searching all bills for challenge with ID: {}", challengeId);

        List<Bill> bills = billRepository.getBillsForChallenge(challengeId);
        List<GetBillResponse> billResponses = bills.stream()
                .map(billMapper::mapCategoryToResponse)
                .collect(Collectors.toList());
        GetChallengeFinanceResponse response = new GetChallengeFinanceResponse(billResponses);

        log.info("Found {} bills", bills.size());
        return response;
    }

    /**
     * Get all bills for a specific shooter.
     *
     * @param shooterId
     * @return bills
     */
    public GetShooterFinanceResponse getShooterFinances(Long shooterId) {
        log.info("Searching all bills for shooter with ID: {}", shooterId);

        List<Bill> bills = billRepository.getBillsForShooter(shooterId);
        List<GetBillResponse> billResponses = bills.stream()
                .map(billMapper::mapCategoryToResponse)
                .collect(Collectors.toList());
        GetShooterFinanceResponse response = new GetShooterFinanceResponse(billResponses);

        log.info("Found {} bills", bills.size());
        return response;
    }

}
