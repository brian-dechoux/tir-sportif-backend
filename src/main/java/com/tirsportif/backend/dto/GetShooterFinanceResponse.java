package com.tirsportif.backend.dto;

import lombok.Value;

import java.util.List;

@Value
public class GetShooterFinanceResponse {

    String lastname;

    String firstname;

    List<GetShooterBillResponse> unpaidBills;

    List<GetShooterBillResponse> participationBills;

    List<GetShooterBillResponse> licenseBills;

}
