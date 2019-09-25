package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Parameters inherent to categories with disciplines.
 * These are number of series and shots.
 */
@Data
@NoArgsConstructor
@Entity(name = "categoriesDisciplinesParameters")
public class CategoriesDisciplinesParameters {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Positive
    int nbSeries;

    @NotNull
    @Positive
    int nbShotsPerSerie;

    @NonNull
    @NotNull
    @ManyToOne
    @JoinColumn(name = "categoryId")
    Category category;

    @NonNull
    @NotNull
    @ManyToOne
    @JoinColumn(name = "disciplineId")
    Discipline discipline;

}
