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

    // Can be problematic stuff like 2A
    String number;

    String zip;

    @NonNull
    @NotNull
    String city;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "countryId")
    Country country;

}
