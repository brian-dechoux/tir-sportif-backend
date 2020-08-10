package com.tirsportif.backend.model;

import com.tirsportif.backend.utils.Regexes;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
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

    @ManyToOne
    @JoinColumn(name = "clubId")
    Club club;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    Category category;

    @Email(regexp = Regexes.EMAIL)
    String email;
}
