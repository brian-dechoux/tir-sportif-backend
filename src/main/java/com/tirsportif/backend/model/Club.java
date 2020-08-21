package com.tirsportif.backend.model;

import com.tirsportif.backend.utils.Regexes;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "club")
public class Club {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @NonNull
    @NotNull
    String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId")
    Address address;

    @Email(regexp = Regexes.EMAIL)
    String email;

}
