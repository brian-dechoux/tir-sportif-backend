package com.tirsportif.backend.model;

import com.tirsportif.backend.utils.Regexes;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@Entity(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @NonNull
    @NotNull
    @Pattern(regexp = Regexes.OFFICIAL_CATEGORY_LABEL)
    String label;

    @NonNull
    @NotNull
    String code;

    @NonNull
    @NotNull
    @Enumerated(value = EnumType.STRING)
    Gender gender;

    @Positive
    Integer ageMin;

    @Positive
    Integer ageMax;

    @Enumerated(value = EnumType.STRING)
    Gcc gccMax;

}
