package com.tirsportif.backend.exception;

import com.tirsportif.backend.error.Error;

public class BadRequestException extends ErrorException {

    public BadRequestException(Error error, String... args) {
        super(error, args);
    }

}
