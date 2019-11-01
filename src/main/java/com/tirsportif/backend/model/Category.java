package com.tirsportif.backend.model;

import com.tirsportif.backend.utils.Regexes;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity(name = "category")
// To avoid StackOverflow error when retrieving data from database (recursive dependency from object POV):
@EqualsAndHashCode(exclude="challenges")
@ToString(exclude = "challenges")
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @NonNull
    @NotNull
    @Pattern(regexp = Regexes.OFFICIAL_CATEGORY_LABEL)
    String label;

    @NonNull
    @NotNull
    String code;

    @NonNull
    @NotNull
    @Enumerated(value = EnumType.STRING)
    Gender gender;

    @Positive
    Integer ageMin;

    @Positive
    Integer ageMax;

    @ManyToMany(mappedBy = "categories")
    Set<Challenge> challenges;

}
