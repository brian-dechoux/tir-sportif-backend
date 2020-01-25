package com.tirsportif.backend.configuration.controlleradvice;

import com.tirsportif.backend.dto.ErrorResponse;
import com.tirsportif.backend.error.GenericClientError;
import com.tirsportif.backend.exception.BadRequestErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

@Slf4j
@Order(1)
@ControllerAdvice
public class ValidationControllerAdvice extends BaseControllerAdvice {

    // FIXME Lang is not well handled here. A solution: https://stackoverflow.com/a/42906916/4675568
    private String getFormattedValidationErrors(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidationException(MethodArgumentNotValidException exception, WebRequest request, HttpServletResponse response) {
        log.error("Validation exception in controller", exception);
        String validationErrors = getFormattedValidationErrors(exception);
        BadRequestErrorException genericException = new BadRequestErrorException(GenericClientError.VALIDATION_FAILED, validationErrors);
        return buildErrorResponse(genericException);
    }

}
