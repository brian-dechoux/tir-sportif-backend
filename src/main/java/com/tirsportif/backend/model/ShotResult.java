package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Shot result for a serie.
 * If no order, it means the result concerns the whole serie.
 * This happens when using electronic target for a participation.
 * Also, this result should not be considered if there are full results.
 */
@Data
@NoArgsConstructor
@Entity(name = "shotResult")
public class ShotResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Number of the serie.
     */
    @NotNull
    @Positive
    int serie;

    /**
     * Order of the shot.
     * Let's say we've got a serie of 10 shots, then order will be [1-10].
     * If null, we consider it as a whole serie result.
     */
    @Positive
    Integer order;

    @NotNull
    @Positive
    double points;

    @NonNull
    @NotNull
    @ManyToOne
    @JoinColumn(name = "participationId")
    Participation participation;

}
