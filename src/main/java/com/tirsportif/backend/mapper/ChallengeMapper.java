package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetChallengeResponse;
import com.tirsportif.backend.dto.GetParticipationResponse;
import com.tirsportif.backend.dto.ResolvedCreateChallengeRequest;
import com.tirsportif.backend.model.Challenge;
import com.tirsportif.backend.model.Participation;
import org.springframework.stereotype.Component;

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

    public GetParticipationResponse mapParticipationToResponse(Participation participation) {
        return GetParticipationResponse.builder()
                .challenge(mapChallengeToResponse(participation.getChallenge()))
                .shooter(shooterMapper.mapShooterToResponse(participation.getShooter()))
                .category(categoryMapper.mapCategoryToResponse(participation.getCategory()))
                .discipline(disciplineMapper.mapDisciplineToResponse(participation.getDiscipline()))
                .useElectronicTarget(participation.isUseElectronicTarget())
                .paid(participation.isPaid())
                .outrank(participation.isOutrank())
                .build();
    }

}
