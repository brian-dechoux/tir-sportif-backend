package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

@Value
public class AuthenticationResponse {

    @NonNull
    String jwtToken;

}
