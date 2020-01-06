package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

@Value
public class ErrorResponse {

    @NonNull
    String code;

    @NonNull
    String message;

}
