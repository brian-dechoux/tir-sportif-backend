package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetChallengeResponse;
import com.tirsportif.backend.dto.ResolvedCreateChallengeRequest;
import com.tirsportif.backend.model.Challenge;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ChallengeMapper {

    private final AddressMapper addressMapper;
    private final ClubMapper clubMapper;
    private final CategoryMapper categoryMapper;
    private final DisciplineMapper disciplineMapper;

    public ChallengeMapper(AddressMapper addressMapper, ClubMapper clubMapper, CategoryMapper categoryMapper, DisciplineMapper disciplineMapper) {
        this.addressMapper = addressMapper;
        this.clubMapper = clubMapper;
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
                    .map(categoryMapper::mapClubToResponse)
                    .collect(Collectors.toSet()),
                challenge.getDisciplines().stream()
                        .map(disciplineMapper::mapDisciplineToResponse)
                        .collect(Collectors.toSet())
        );
    }

}
