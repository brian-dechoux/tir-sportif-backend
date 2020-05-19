package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetParticipantResponse;
import com.tirsportif.backend.model.projection.Participant;
import org.springframework.stereotype.Component;

@Component
public class ParticipationMapper {

    public GetParticipantResponse mapParticipantToResponse(Participant participant) {
        return new GetParticipantResponse(
                participant.getId(),
                participant.getLastname(),
                participant.getFirstname(),
                participant.getClubId(),
                participant.getClubName()
        );
    }

}
