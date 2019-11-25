package com.tirsportif.backend.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity(name = "discipline")
// To avoid StackOverflow error when retrieving data from database (recursive dependency from object POV):
@EqualsAndHashCode(exclude="challenges")
@ToString(exclude = "challenges")
public class Discipline {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
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
    boolean isDecimalResult;

    @ManyToMany(mappedBy = "disciplines")
    Set<Challenge> challenges;

}
