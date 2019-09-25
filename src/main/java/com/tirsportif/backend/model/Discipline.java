package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity(name = "discipline")
public class Discipline {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;

    @NonNull
    @NotNull
    String label;

    @NonNull
    @NotNull
    String code;

    @NotNull
    @Positive
    int nbSeries;

    @NotNull
    @Positive
    int nbShotsPerSerie;

    /**
     * Some discipline uses integer, some others decimal.
     */
    @NotNull
    boolean usesDecimalResults;

    @ManyToMany(mappedBy = "disciplines")
    Set<Challenge> challenges;

}
