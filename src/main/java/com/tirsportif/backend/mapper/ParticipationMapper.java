package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetParticipantResponse;
import com.tirsportif.backend.dto.GetParticipationResponse;
import com.tirsportif.backend.dto.GetShooterParticipationsResponse;
import com.tirsportif.backend.model.Bill;
import com.tirsportif.backend.model.Participation;
import com.tirsportif.backend.model.Shooter;
import com.tirsportif.backend.model.projection.Participant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ParticipationMapper {

    private final ShooterMapper shooterMapper;
    private final DisciplineMapper disciplineMapper;

    public ParticipationMapper(ShooterMapper shooterMapper, DisciplineMapper disciplineMapper) {
        this.shooterMapper = shooterMapper;
        this.disciplineMapper = disciplineMapper;
    }

    public GetParticipantResponse mapParticipantToResponse(Participant participant) {
        return new GetParticipantResponse(
                participant.getId(),
                participant.getLastname(),
                participant.getFirstname(),
                participant.getClubId(),
                participant.getClubName()
        );
    }

    public GetParticipationResponse mapParticipationToResponse(Participation participation) {
        boolean paid = Optional.ofNullable(participation.getBill())
                .map(Bill::isPaid)
                .orElse(false);
        return GetParticipationResponse.builder()
                .id(participation.getId())
                .discipline(disciplineMapper.mapDisciplineToResponse(participation.getDiscipline()))
                .useElectronicTarget(participation.isUseElectronicTarget())
                .paid(paid)
                .outrank(participation.isOutrank())
                .build();
    }

    public GetShooterParticipationsResponse mapShooterAndParticipationsToResponse(Shooter shooter, List<Participation> participations) {
        List<GetParticipationResponse> participationResponses = participations.stream()
                .map(this::mapParticipationToResponse)
                .collect(Collectors.toList());
        return GetShooterParticipationsResponse.builder()
                .shooter(shooterMapper.mapShooterToResponse(shooter))
                .participations(participationResponses)
                .build();
    }

}
