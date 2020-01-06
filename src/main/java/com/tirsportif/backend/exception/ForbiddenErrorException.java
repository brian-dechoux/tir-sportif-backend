package com.tirsportif.backend.exception;

import com.tirsportif.backend.error.Error;

public class ForbiddenErrorException extends ErrorException {

    public ForbiddenErrorException(Error error, String... args) {
        super(error, args);
    }

}
