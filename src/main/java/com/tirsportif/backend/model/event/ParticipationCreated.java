package com.tirsportif.backend.model.event;

import com.tirsportif.backend.model.Participation;
import lombok.Value;

@Value
public class ParticipationCreated {

    public ParticipationCreated(Participation participation) {
        this.participation = participation;
        this.paid = participation.isPaid();
    }

    Participation participation;

    boolean paid;

}
