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
    @JoinColumn(name = "disciplineId")
    Discipline discipline;

    @NotNull
    boolean useElectronicTarget;

    /**
     * Used to generate the bill after this instance creation.
     * Then, the attribute is passed along to the {@link Bill} instance.
     */
    @Transient
    boolean paid;

    @OneToOne(mappedBy = "participation")
    Bill bill;

    @NotNull
    boolean outrank;

}
