package com.tirsportif.backend.exception;

import com.tirsportif.backend.error.Error;

public class InternalServerErrorException extends ErrorException {

    public InternalServerErrorException(Error error, String... args) {
        super(error, args);
    }

}
