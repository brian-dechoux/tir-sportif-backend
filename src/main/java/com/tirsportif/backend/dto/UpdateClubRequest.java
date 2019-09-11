package com.tirsportif.backend.dto;

import lombok.Data;

@Data
public class UpdateClubRequest {

    String name;

    CreateAddressRequest address;

}
