package com.tirsportif.backend.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "participation")
@EqualsAndHashCode(exclude={"shotResults", "bill"})
@ToString(exclude = {"shotResults", "bill"})
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "participation")
    Set<ShotResult> shotResults;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "participation", cascade = CascadeType.REMOVE)
    Bill bill;

    @NotNull
    boolean useElectronicTarget;

    /**
     * Used to generate the bill after this instance creation.
     * Then, the attribute is passed along to the {@link Bill} instance.
     */
    @Transient
    boolean paid;

    @NotNull
    boolean outrank;

}
