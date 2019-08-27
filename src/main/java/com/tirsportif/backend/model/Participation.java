package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
public class Participation {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;

    @NonNull
    @NotNull
    @ManyToOne(optional = false)
    Shooter shooter;

    @NonNull
    @NotNull
    @ManyToOne(optional = false)
    Challenge challenge;

    @NonNull
    @NotNull
    @ManyToOne(optional = false)
    Category category;

    @NonNull
    @NotNull
    @ManyToOne(optional = false)
    Discipline discipline;

    @NotNull
    boolean useElectronicTarget;

    @NotNull
    boolean paid;

    @NotNull
    boolean outrank;

}
