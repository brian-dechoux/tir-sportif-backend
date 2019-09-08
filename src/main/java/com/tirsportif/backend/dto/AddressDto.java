package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddressDto {

    @NotNull
    String street;

    @NotNull
    int number;

    String zip;

    @NotNull
    String city;

    CountryDto country;

}
