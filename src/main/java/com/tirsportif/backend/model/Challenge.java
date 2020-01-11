package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity(name = "challenge")
public class Challenge {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @NonNull
    @NotNull
    String name;

    @NonNull
    @NotNull
    LocalDateTime startDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId")
    Address address;

    @ManyToOne
    @JoinColumn(name = "organiserClubId")
    Club organiserClub;

    @ManyToMany
    @JoinTable(
            name = "challengeCategories",
            joinColumns = @JoinColumn(name = "challengeId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "categoryId", referencedColumnName = "id")
    )
    Set<Category> categories;

    @ManyToMany
    @JoinTable(
            name = "challengeDisciplines",
            joinColumns = @JoinColumn(name = "challengeId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "disciplineId", referencedColumnName = "id")
    )
    Set<Discipline> disciplines;

}
