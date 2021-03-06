package com.tirsportif.backend.model.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ShotResultKey implements Serializable {

    /**
     * Number of the serie.
     */
    @NotNull
    @Positive
    int serieNumber;

    /**
     * Order of the shot.
     * Let's say we've got a serie of 10 shots, then order will be [1-10].
     * Special cases:
     * * -1: Calculated serie total
     * * -2: Manual serie total (user input)
     */
    int shotNumber;

    long participationId;

}
