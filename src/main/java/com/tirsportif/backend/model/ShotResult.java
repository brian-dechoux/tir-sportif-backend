package com.tirsportif.backend.model;

import com.tirsportif.backend.model.key.ShotResultKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Shot result for a serie.
 * If no order, it means the result concerns the whole serie.
 * This happens when using electronic target for a participation.
 * Also, this result should not be considered if there are full results.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "shotResult")
public class ShotResult {

    @EmbeddedId
    ShotResultKey id;

    @NotNull
    @Positive
    double points;

    @NonNull
    @NotNull
    @ManyToOne
    @JoinColumn(name = "participationId", insertable = false, updatable = false)
    Participation participation;

}
