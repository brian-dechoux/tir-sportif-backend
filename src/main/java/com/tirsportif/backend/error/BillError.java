package com.tirsportif.backend.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BillError implements Error {

    NO_PRICE_FOR_PARAMETERS("BI0001", "Impossible de générer la facture, la référence de prix est introuvable: %s, %s");

    private String code;
    private String message;

}
