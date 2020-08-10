package com.tirsportif.backend.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity(name = "challenge")
@EqualsAndHashCode(exclude="participations")
@ToString(exclude = "participations")
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId")
    Address address;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "challenge")
    Set<Participation> participations;

    @ManyToOne
    @JoinColumn(name = "organiserClubId")
    Club organiserClub;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "challengeCategories",
            joinColumns = @JoinColumn(name = "challengeId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "categoryId", referencedColumnName = "id")
    )
    Set<Category> categories;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "challengeDisciplines",
            joinColumns = @JoinColumn(name = "challengeId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "disciplineId", referencedColumnName = "id")
    )
    Set<Discipline> disciplines;

}
