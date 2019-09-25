package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity(name = "shooter")
public class Shooter {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @NonNull
    @NotNull
    String lastname;

    @NonNull
    @NotNull
    String firstname;

    @Column(name = "birthDate", columnDefinition = "DATE")
    LocalDate birthDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId")
    Address address;

    @ManyToOne
    @JoinColumn(name = "clubId")
    Club club;

    @ManyToOne
    @JoinColumn(name = "category")
    Category category;

}
