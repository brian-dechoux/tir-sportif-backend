package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthenticationError implements Error {

    EXPIRED_TOKEN("AUTH001", "The current authentication token is expired. Please login again."),
    WRONG_FORMAT_TOKEN("AUTH002", "The current authentication token has wrong format. It must be a valid JWT of the form: 'Bearer $tokenValue'"),
    WRONG_USERNAME("AUTH003", "The provided username is wrong."),
    WRONG_PASSWORD("AUTH004", "The provided password is wrong."),
    UNKNOWN_TOKEN("AUTH005", "Unknown token. Please login again.");

    private String code;
    private String message;

}
