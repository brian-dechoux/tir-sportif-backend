package com.tirsportif.backend.dto;

import lombok.Value;

@Value
public class GetClubResponse {

    String name;

    GetAddressResponse address;

}
