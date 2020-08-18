package com.tirsportif.backend.dto;

import lombok.Value;

@Value
public class GetClubResponse {

    Long id;

    String name;

    GetAddressResponse address;

    String email;

}
