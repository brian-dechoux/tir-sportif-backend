package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@Entity
public class Price {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;

    @NonNull
    @NotNull
    @Enumerated(value = EnumType.STRING)
    PriceType type;

    @NotNull
    boolean forLicenseeOnly;

    @NotNull
    @Positive
    double value;

    @ManyToOne
    @JoinColumn(name = "clubId")
    Club club;

}
