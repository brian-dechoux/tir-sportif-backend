package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
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
    @Column(name = "startDate", columnDefinition = "TIME WITH TIME ZONE")
    OffsetDateTime startDate;

    @ManyToOne
    @JoinColumn(name = "addressId")
    Address address;

    @ManyToOne
    @JoinColumn(name = "organiserClubId")
    Club organiserClubId;

    @ManyToMany
    @JoinTable(name = "challengeDiscipline")
    Set<Discipline> disciplines;

    @ManyToMany
    @JoinTable(name = "challengeCategory")
    Set<Category> categories;

}
