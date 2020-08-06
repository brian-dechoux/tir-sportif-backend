package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.model.Category;
import com.tirsportif.backend.model.Challenge;
import com.tirsportif.backend.model.Discipline;
import com.tirsportif.backend.model.Participation;
import com.tirsportif.backend.model.projection.ChallengeListElementProjection;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ChallengeMapper {

    private final AddressMapper addressMapper;
    private final ClubMapper clubMapper;
    private final CategoryMapper categoryMapper;
    private final ParticipationMapper participationMapper;
    private final DisciplineMapper disciplineMapper;

    public ChallengeMapper(AddressMapper addressMapper, ClubMapper clubMapper, ParticipationMapper participationMapper, CategoryMapper categoryMapper, DisciplineMapper disciplineMapper) {
        this.addressMapper = addressMapper;
        this.clubMapper = clubMapper;
        this.participationMapper = participationMapper;
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
                        .sorted(Comparator.comparingLong(Category::getId))
                        .map(categoryMapper::mapCategoryToResponse)
                        .collect(Collectors.toList()),
                challenge.getDisciplines().stream()
                        .sorted(Comparator.comparingLong(Discipline::getId))
                        .map(disciplineMapper::mapDisciplineToResponse)
                        .collect(Collectors.toList())
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
                        .sorted(Comparator.comparingLong(Category::getId))
                        .map(categoryMapper::mapCategoryToResponse)
                        .collect(Collectors.toSet()),
                challenge.getDisciplines().stream()
                        .sorted(Comparator.comparingLong(Discipline::getId))
                        .map(disciplineMapper::mapDisciplineToResponse)
                        .collect(Collectors.toSet()),
                participations.stream()
                        .map(participationMapper::mapParticipationToResponse)
                        .collect(Collectors.toSet())
        );
    }

    public GetChallengeListElementResponse mapChallengeListElementToResponse(ChallengeListElementProjection challengeListElementProjection) {
        return new GetChallengeListElementResponse(
                challengeListElementProjection.getId(),
                challengeListElementProjection.getName(),
                challengeListElementProjection.getStartDate(),
                challengeListElementProjection.getCity(),
                challengeListElementProjection.getNbShooters()
        );
    }

}
