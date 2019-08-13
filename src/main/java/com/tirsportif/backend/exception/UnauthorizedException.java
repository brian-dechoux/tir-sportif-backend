package com.tirsportif.backend.exception;

import com.tirsportif.backend.error.Error;

public class UnauthorizedException extends ErrorException {

    public UnauthorizedException(Error error, String... args) {
        super(error, args);
    }

}
