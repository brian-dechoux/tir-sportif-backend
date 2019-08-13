package com.tirsportif.backend.configuration;

import com.tirsportif.backend.dto.ErrorResponse;
import com.tirsportif.backend.error.SystemError;
import com.tirsportif.backend.exception.BadRequestException;
import com.tirsportif.backend.exception.ForbiddenException;
import com.tirsportif.backend.exception.InternalServerErrorException;
import com.tirsportif.backend.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class CustomControllerAdvice extends BaseControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleBadRequestException(BadRequestException exception, WebRequest request, HttpServletResponse response) {
        log.error("BadRequestException in controller", exception);
        return buildErrorResponse(exception);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleForbiddenException(ForbiddenException exception, WebRequest request, HttpServletResponse response) {
        log.error("ForbiddenException in controller", exception);
        return buildErrorResponse(exception);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleUnauthorizedException(UnauthorizedException exception, WebRequest request, HttpServletResponse response) {
        log.error("UnauthorizedException in controller", exception);
        return buildErrorResponse(exception);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleInternalServerErrorException(InternalServerErrorException exception, WebRequest request, HttpServletResponse response) {
        log.error("InternalServerErrorException in controller", exception);
        return buildErrorResponse(exception);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleException(Exception exception, WebRequest request, HttpServletResponse response) {
        log.error("Generic Exception in controller", exception);
        InternalServerErrorException genericException = new InternalServerErrorException(SystemError.TECHNICAL_ERROR, exception.getMessage());
        return buildErrorResponse(genericException);
    }

}
