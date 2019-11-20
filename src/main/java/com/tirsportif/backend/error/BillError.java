package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BillError implements Error {

    NO_PRICE_FOR_PARAMETERS("BI0001", "Cannot generate a bill for the provided parameters. Type: %s, For licensee: %s");

    private String code;
    private String message;

}
