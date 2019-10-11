package com.tirsportif.backend.dto;

import lombok.NonNull;
import lombok.Value;

@Value
public class GetDisciplineResponse {

    @NonNull
    Long id;

    @NonNull
    String label;

    @NonNull
    String code;

    boolean useDecimalResults;

}
