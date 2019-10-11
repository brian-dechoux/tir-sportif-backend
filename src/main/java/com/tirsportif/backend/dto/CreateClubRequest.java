package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateClubRequest {

    @NotNull
    @NotEmpty
    String name;

    @NotNull
    CreateAddressRequest address;

}
