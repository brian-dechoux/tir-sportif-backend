package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Shooter {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;

    @NonNull
    @NotNull
    String lastname;

    @NonNull
    @NotNull
    String firstname;

    @Column(name = "birthDate", columnDefinition = "DATE")
    LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "addressId")
    Address address;

    @ManyToOne
    @JoinColumn(name = "clubId")
    Club club;



}
