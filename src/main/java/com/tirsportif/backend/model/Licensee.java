package com.tirsportif.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@Entity
public class Licensee {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id;

    @NotNull
    int badgeNumber;

    int lockerNumber;

    @NonNull
    @NotNull
    @Column(name = "subscriptionDate", columnDefinition = "TIME WITH TIME ZONE")
    OffsetDateTime subscriptionDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shooterId")
    Shooter shooter;

}
