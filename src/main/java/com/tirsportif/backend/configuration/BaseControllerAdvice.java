package com.tirsportif.backend.configuration;

import com.tirsportif.backend.dto.ErrorResponse;
import com.tirsportif.backend.exception.ErrorException;

abstract class BaseControllerAdvice {

    ErrorResponse buildErrorResponse(ErrorException errorException) {
        return new ErrorResponse(errorException.getCode(), errorException.getMessage());
    }

}
