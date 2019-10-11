package com.tirsportif.backend.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "licensee")
public class Licensee {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @NotNull
    int badgeNumber;

    int lockerNumber;

    // TODO this should be a date, not datetime
    @NonNull
    @NotNull
    @Column(name = "subscriptionDate", columnDefinition = "TIME WITH TIME ZONE")
    OffsetDateTime subscriptionDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shooterId")
    Shooter shooter;

}
