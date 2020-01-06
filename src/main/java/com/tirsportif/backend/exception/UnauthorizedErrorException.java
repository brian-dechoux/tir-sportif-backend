package com.tirsportif.backend.exception;

import com.tirsportif.backend.error.Error;

public class UnauthorizedErrorException extends ErrorException {

    public UnauthorizedErrorException(Error error, String... args) {
        super(error, args);
    }

}
