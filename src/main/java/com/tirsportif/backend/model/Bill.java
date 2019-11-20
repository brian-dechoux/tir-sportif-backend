package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@Entity(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @Positive
    double value;

    boolean paid;

    @ManyToOne
    @JoinColumn(name = "participationId")
    Participation participation;

    @ManyToOne
    @JoinColumn(name = "licenseeId")
    Licensee licensee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "priceId")
    Price price;

}
