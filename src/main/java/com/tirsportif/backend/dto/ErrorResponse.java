package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

@Value
public class ErrorResponse {

    @NonNull
    String error;

    @NonNull
    String message;

}
