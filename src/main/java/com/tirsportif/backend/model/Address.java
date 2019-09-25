package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @NonNull
    @NotNull
    String street;

    // Can be problematic stuff like 2A
    String number;

    String zip;

    @NonNull
    @NotNull
    String city;

    @ManyToOne
    @JoinColumn(name = "countryId")
    Country country;

}
