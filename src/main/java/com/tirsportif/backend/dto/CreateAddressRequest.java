package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class CreateAddressRequest {

    @NotNull
    String street;

    String number;

    String zip;

    @NotNull
    String city;

    @NotNull
    @Positive
    Long countryId;

}
