package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetShooterBillResponse;
import com.tirsportif.backend.dto.GetShooterFinanceResponse;
import com.tirsportif.backend.dto.GetShooterWithFinancesListElementResponse;
import com.tirsportif.backend.model.PriceType;
import com.tirsportif.backend.model.projection.ShooterBillProjection;
import com.tirsportif.backend.model.projection.ShooterWithBillsListElementProjection;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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

    public GetShooterFinanceResponse mapShooterBillsToResponse(List<ShooterBillProjection> bills) {
         Map<Boolean, Map<PriceType, List<GetShooterBillResponse>>> shooterFinance= bills.stream()
                .map(this::mapShooterBillToResponse)
                .collect(Collectors.groupingBy(GetShooterBillResponse::isPaid, Collectors.groupingBy(GetShooterBillResponse::getPriceType)));

         List<GetShooterBillResponse> unpaidBills = shooterFinance.get(false).values().stream()
                 .flatMap(Collection::stream)
                 .collect(Collectors.toList());
         List<GetShooterBillResponse> participationBills = shooterFinance.get(true).get(PriceType.CHALLENGE);
         List<GetShooterBillResponse> licenseBills = shooterFinance.get(true).get(PriceType.LICENSE);

         return new GetShooterFinanceResponse(
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
                bill.getType(),
                bill.getChallengeName(),
                bill.getChallengeStartDate(),
                bill.getLicenseSubscriptionDate()
        );
    }

}
