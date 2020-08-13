package com.tirsportif.backend.service;

import com.tirsportif.backend.error.BillError;
import com.tirsportif.backend.exception.InternalServerErrorException;
import com.tirsportif.backend.model.*;
import com.tirsportif.backend.model.event.LicenseSubscriptionEvent;
import com.tirsportif.backend.model.event.ParticipationCreatedEvent;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.BillRepository;
import com.tirsportif.backend.repository.LicenseeRepository;
import com.tirsportif.backend.repository.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
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

    // TODO Evolution: Consider active price only
    @EventListener(value = ParticipationCreatedEvent.class)
    public void generateBill(ParticipationCreatedEvent event) {
        Participation participation = event.getParticipation();
        log.info("Generating bill for participation with ID: {}", participation.getId());

        boolean forLicensee = licenseeRepository.findByShooterId(participation.getShooter().getId()).isPresent();
        Price price = priceRepository.findByTypeAndForLicenseeOnly(PriceType.CHALLENGE, forLicensee)
                .orElseThrow(() -> new InternalServerErrorException(BillError.NO_PRICE_FOR_PARAMETERS, PriceType.CHALLENGE.name(), Boolean.toString(forLicensee)));
        Bill bill  = Bill.builder()
                .value(price.getValue())
                .paid(participation.isPaid())
                .participation(participation)
                .price(price)
                .build();
        billRepository.save(bill);
        log.info("Bill generated");
    }

    // TODO Evolution: Consider active price only
    @EventListener(value = LicenseSubscriptionEvent.class)
    public void generateBill(LicenseSubscriptionEvent event) {
        Licensee licensee = event.getLicensee();
        log.info("Generating bill for licensee subscription with ID: {}", licensee.getId());

        // FIXME Is it cheaper to renew ?
        Price price = priceRepository.findByTypeAndForLicenseeOnly(PriceType.LICENSE, false)
                .orElseThrow(() -> new InternalServerErrorException(BillError.NO_PRICE_FOR_PARAMETERS, PriceType.LICENSE.name(), Boolean.toString(false)));
        // FIXME Is it already paid ?
        Bill bill  = Bill.builder()
                .value(price.getValue())
                .paid(true)
                .licensee(licensee)
                .price(price)
                .build();
        billRepository.save(bill);
        log.info("Bill generated");
    }

}
