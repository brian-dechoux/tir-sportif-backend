package com.tirsportif.backend.exception;

import com.tirsportif.backend.error.Error;

public class BadRequestErrorException extends ErrorException {

    public BadRequestErrorException(Error error, String... args) {
        super(error, args);
    }

}
