package com.tirsportif.backend.dto;

import lombok.Data;

@Data
public class GetAddressResponse {

    String street;

    String number;

    String zip;

    String city;

    String countryCode;

    String countryName;

}
