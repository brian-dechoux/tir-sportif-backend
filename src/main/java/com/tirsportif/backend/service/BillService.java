package com.tirsportif.backend.service;

import com.tirsportif.backend.error.BillError;
import com.tirsportif.backend.exception.InternalServerErrorException;
import com.tirsportif.backend.model.Bill;
import com.tirsportif.backend.model.Participation;
import com.tirsportif.backend.model.Price;
import com.tirsportif.backend.model.PriceType;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.BillRepository;
import com.tirsportif.backend.repository.LicenseeRepository;
import com.tirsportif.backend.repository.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BillService extends AbstractService {

    private final BillRepository billRepository;
    private final PriceRepository priceRepository;
    private final LicenseeRepository licenseeRepository;

    public BillService(ApiProperties apiProperties, BillRepository billRepository, PriceRepository priceRepository, LicenseeRepository licenseeRepository) {
        super(apiProperties);
        this.billRepository = billRepository;
        this.priceRepository = priceRepository;
        this.licenseeRepository = licenseeRepository;
    }

    // TODO async event ?
    public void generateBill(Participation participation) {
        log.info("Generating bill for participation with ID: {}", participation.getId());
        boolean forLicensee = licenseeRepository.findByShooterId(participation.getShooter().getId()).isPresent();
        Price price = priceRepository.findByTypeAndForLicenseeOnly(PriceType.CHALLENGE, forLicensee)
                .orElseThrow(() -> new InternalServerErrorException(BillError.NO_PRICE_FOR_PARAMETERS, PriceType.CHALLENGE.name(), Boolean.toString(forLicensee)));
        Bill bill  = Bill.builder()
                .value(price.getValue())
                .participation(participation)
                .price(price)
                .build();
        billRepository.save(bill);
        log.info("Bill generated");
    }

}
