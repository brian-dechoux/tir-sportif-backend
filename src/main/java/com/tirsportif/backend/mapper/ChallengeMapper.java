package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.model.Challenge;
import com.tirsportif.backend.model.Participation;
import com.tirsportif.backend.model.projection.ChallengeListElement;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ChallengeMapper {

    private final AddressMapper addressMapper;
    private final ClubMapper clubMapper;
    private final ShooterMapper shooterMapper;
    private final CategoryMapper categoryMapper;
    private final DisciplineMapper disciplineMapper;

    public ChallengeMapper(AddressMapper addressMapper, ClubMapper clubMapper, ShooterMapper shooterMapper, CategoryMapper categoryMapper, DisciplineMapper disciplineMapper) {
        this.addressMapper = addressMapper;
        this.clubMapper = clubMapper;
        this.shooterMapper = shooterMapper;
        this.categoryMapper = categoryMapper;
        this.disciplineMapper = disciplineMapper;
    }

    public Challenge mapCreateChallengeDtoToChallenge(ResolvedCreateChallengeRequest request) {
        Challenge challenge = new Challenge();
        challenge.setName(request.getName());
        challenge.setStartDate(request.getStartDate());
        challenge.setAddress(addressMapper.mapAddressDtoToAddress(request.getAddress()));
        challenge.setOrganiserClub(request.getOrganiserClub());
        challenge.setCategories(request.getCategories());
        challenge.setDisciplines(request.getDisciplines());
        return challenge;
    }

    public Challenge mapUpdateChallengeDtoToChallenge(Challenge challenge, ResolvedUpdateChallengeRequest request) {
        challenge.setName(
                request.getName()
                        .orElse(challenge.getName())
        );
        challenge.setStartDate(
                request.getStartDate()
                        .orElse(challenge.getStartDate())
        );
        challenge.setAddress(
                request.getAddress()
                        .map(addressMapper::mapAddressDtoToAddress)
                        .orElse(challenge.getAddress())
        );
        challenge.setOrganiserClub(
                request.getOrganiserClub()
                        .orElse(challenge.getOrganiserClub())
        );
        challenge.setCategories(
                request.getCategories()
                        .orElse(challenge.getCategories())
        );
        challenge.setDisciplines(
                request.getDisciplines()
                        .orElse(challenge.getDisciplines())
        );
        return challenge;
    }

    public GetChallengeResponse mapChallengeToResponse(Challenge challenge) {
        return new GetChallengeResponse(
                challenge.getId(),
                challenge.getName(),
                challenge.getStartDate(),
                addressMapper.mapAddressToDto(challenge.getAddress()),
                clubMapper.mapClubToResponse(challenge.getOrganiserClub()),
                challenge.getCategories().stream()
                        .map(categoryMapper::mapCategoryToResponse)
                        .collect(Collectors.toSet()),
                challenge.getDisciplines().stream()
                        .map(disciplineMapper::mapDisciplineToResponse)
                        .collect(Collectors.toSet())
        );
    }

    public GetChallengeWithParticipationsResponse mapChallengeAndParticipationsToResponse(Challenge challenge, Set<Participation> participations) {
        return new GetChallengeWithParticipationsResponse(
                challenge.getId(),
                challenge.getName(),
                challenge.getStartDate(),
                addressMapper.mapAddressToDto(challenge.getAddress()),
                clubMapper.mapClubToResponse(challenge.getOrganiserClub()),
                challenge.getCategories().stream()
                        .map(categoryMapper::mapCategoryToResponse)
                        .collect(Collectors.toSet()),
                challenge.getDisciplines().stream()
                        .map(disciplineMapper::mapDisciplineToResponse)
                        .collect(Collectors.toSet()),
                participations.stream()
                        .map(this::mapParticipationToResponse)
                        .collect(Collectors.toSet())
        );
    }

    public GetChallengeListElementResponse mapChallengeListElementToResponse(ChallengeListElement challengeListElement) {
        return new GetChallengeListElementResponse(
                challengeListElement.getId(),
                challengeListElement.getName(),
                challengeListElement.getStartDate(),
                challengeListElement.getCity(),
                challengeListElement.getNbShooters()
        );
    }

    public GetParticipationResponse mapParticipationToResponse(Participation participation) {
        return GetParticipationResponse.builder()
                .shooter(shooterMapper.mapShooterToResponse(participation.getShooter()))
                .category(categoryMapper.mapCategoryToResponse(participation.getCategory()))
                .discipline(disciplineMapper.mapDisciplineToResponse(participation.getDiscipline()))
                .useElectronicTarget(participation.isUseElectronicTarget())
                .paid(participation.isPaid())
                .outrank(participation.isOutrank())
                .build();
    }

}
