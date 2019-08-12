package com.tirsportif.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class JwtRequest {

    @NotEmpty
    String username;

    @NotEmpty
    String password;

}
