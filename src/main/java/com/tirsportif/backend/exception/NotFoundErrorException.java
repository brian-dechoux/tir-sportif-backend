package com.tirsportif.backend.exception;

import com.tirsportif.backend.error.Error;

public class NotFoundErrorException extends ErrorException {

    public NotFoundErrorException(Error error, String... args) {
        super(error, args);
    }

}
