package com.tirsportif.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "bill")
@Builder(toBuilder = true)
public class Bill {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    OffsetDateTime creationDate;

    /**
     * Uncorrelated value from the single price.
     * Other factors may be implied in calculation.
     */
    @Positive
    double value;

    boolean paid;

    OffsetDateTime paidDate;

    @OneToOne
    @JoinColumn(name = "participationId", referencedColumnName = "id")
    Participation participation;

    @ManyToOne
    @JoinColumn(name = "licenseeId")
    Licensee licensee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "priceId")
    Price price;

}
