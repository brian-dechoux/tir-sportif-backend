package com.tirsportif.backend.dto;

import com.tirsportif.backend.model.Gcc;
import com.tirsportif.backend.model.Gender;
import lombok.NonNull;
import lombok.Value;

@Value
public class GetCategoryResponse {

    @NonNull
    Long id;

    @NonNull
    String label;

    @NonNull
    String code;

    @NonNull
    Gender gender;

    Integer ageMin;

    Integer ageMax;

    Gcc gccMax;

}
