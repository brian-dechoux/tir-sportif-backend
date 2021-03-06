package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;

@Value
public class GetLicenseeListElementResponse {

    @NonNull
    Long id;

    @NonNull
    String lastname;

    @NonNull
    String firstname;

    @NonNull
    LocalDate subscriptionDate;

}
