package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetShooterBillResponse;
import com.tirsportif.backend.dto.GetShooterFinanceResponse;
import com.tirsportif.backend.dto.GetShooterWithFinancesListElementResponse;
import com.tirsportif.backend.model.PriceType;
import com.tirsportif.backend.model.Shooter;
import com.tirsportif.backend.model.projection.ShooterBillProjection;
import com.tirsportif.backend.model.projection.ShooterWithBillsListElementProjection;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BillMapper {

    public GetShooterWithFinancesListElementResponse mapShooterWithBillsProjectionToResponse(ShooterWithBillsListElementProjection projection) {
        return new GetShooterWithFinancesListElementResponse(
                projection.getId(),
                projection.getLastname(),
                projection.getFirstname()
        );
    }

    public GetShooterFinanceResponse mapShooterBillsToResponse(Shooter shooter, List<ShooterBillProjection> bills) {
         Map<Boolean, Map<PriceType, List<GetShooterBillResponse>>> shooterFinance= bills.stream()
                .map(this::mapShooterBillToResponse)
                .collect(Collectors.groupingBy(GetShooterBillResponse::isPaid, Collectors.groupingBy(GetShooterBillResponse::getPriceType)));

         List<GetShooterBillResponse> unpaidBills = Optional.ofNullable(shooterFinance.get(false))
                 .map(unpaids ->
                                 unpaids.values().stream()
                                         .flatMap(Collection::stream)
                                         .collect(Collectors.toList())
                 )
                 .orElse(Collections.emptyList());
         List<GetShooterBillResponse> participationBills = Optional.ofNullable(shooterFinance.get(true))
                 .map(participations -> participations.get(PriceType.CHALLENGE))
                 .orElse(Collections.emptyList());
         List<GetShooterBillResponse> licenseBills = Optional.ofNullable(shooterFinance.get(true))
                 .map(participations -> participations.get(PriceType.LICENSE))
                 .orElse(Collections.emptyList());

         return new GetShooterFinanceResponse(
                 shooter.getLastname(),
                 shooter.getFirstname(),
                 unpaidBills,
                 participationBills,
                 licenseBills
         );
    }

    public GetShooterBillResponse mapShooterBillToResponse(ShooterBillProjection bill) {
        return new GetShooterBillResponse(
                bill.getId(),
                bill.getValue(),
                bill.getPaid(),
                bill.getPaidDate(),
                bill.getType(),
                bill.getChallengeName(),
                bill.getChallengeStartDate(),
                bill.getLicenseSubscriptionDate()
        );
    }

}
