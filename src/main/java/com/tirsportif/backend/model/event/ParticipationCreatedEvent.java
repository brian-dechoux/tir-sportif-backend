package com.tirsportif.backend.model.event;

import com.tirsportif.backend.model.Participation;
import lombok.Value;

@Value
public class ParticipationCreatedEvent {

    Participation participation;

}
