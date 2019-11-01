package com.tirsportif.backend.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "participation")
public class Participation {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @NonNull
    @NotNull
    @ManyToOne
    @JoinColumn(name = "shooterId")
    Shooter shooter;

    @NonNull
    @NotNull
    @ManyToOne
    @JoinColumn(name = "challengeId")
    Challenge challenge;

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

    @NotNull
    boolean useElectronicTarget;

    @NotNull
    boolean paid;

    @NotNull
    boolean outrank;

}
