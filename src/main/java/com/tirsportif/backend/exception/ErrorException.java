package com.tirsportif.backend.exception;

import com.tirsportif.backend.error.Error;
import lombok.Getter;

@Getter
public abstract class ErrorException extends RuntimeException {

    String code;
    String message;

    public ErrorException(Error error, String... args) {
        this.code = error.getCode();
        this.message = String.format(error.getMessage(), args);
    }

}
