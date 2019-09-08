package com.tirsportif.backend.exception;

import com.tirsportif.backend.error.Error;

public class NotFoundException extends ErrorException {

    public NotFoundException(Error error, String... args) {
        super(error, args);
    }

}
