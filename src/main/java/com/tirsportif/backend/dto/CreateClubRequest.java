package com.tirsportif.backend.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class CreateClubRequest {

    @NotNull
    @NotEmpty
    String name;

    @NotNull
    AddressDto address;

}
