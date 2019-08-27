package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;

    @NonNull
    @NotNull
    String street;

    @NotNull
    int number;

    String zip;

    @NonNull
    @NotNull
    String city;

    @ManyToOne
    @JoinColumn(name = "countryId")
    Country country;

}
