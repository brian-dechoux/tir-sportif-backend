package com.tirsportif.backend.configuration;

import com.tirsportif.backend.dto.ErrorResponse;
import com.tirsportif.backend.exception.ErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

abstract class BaseControllerAdvice extends ResponseEntityExceptionHandler {

    ErrorResponse buildErrorResponse(ErrorException errorException) {
        return new ErrorResponse(errorException.getCode(), errorException.getMessage());
    }

}
