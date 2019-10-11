package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetShooterResponse;
import com.tirsportif.backend.dto.ResolvedCreateChallengeRequest;
import com.tirsportif.backend.model.Challenge;
import com.tirsportif.backend.model.Shooter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ChallengeMapper {

    private final AddressMapper addressMapper;
    private final ClubMapper clubMapper;

    public ChallengeMapper(AddressMapper addressMapper, ClubMapper clubMapper) {
        this.addressMapper = addressMapper;
        this.clubMapper = clubMapper;
    }

    public Challenge mapCreateShooterDtoToShooter(ResolvedCreateChallengeRequest request) {
        Challenge challenge = new Challenge();
        challenge.setName(request.getName());
        challenge.setStartDate(request.getStartDate());
        challenge.setAddress(addressMapper.mapAddressDtoToAddress(request.getAddress()));
        challenge.setOrganiserClub(request.getOrganiserClub());
        challenge.setCategories(request.getCategories());
        challenge.setDisciplines(request.getDisciplines());
        return challenge;
    }

    // TODO
    public GetShooterResponse mapShooterToResponse(Shooter shooter) {
        return new GetShooterResponse(
                shooter.getLastname(),
                shooter.getFirstname(),
                shooter.getBirthDate(),
                addressMapper.mapAddressToDto(shooter.getAddress()),
                Optional.ofNullable(shooter.getClub())
                        .map(clubMapper::mapClubToResponse)
                        .orElse(null),
                shooter.getCategory()
        );
    }

}
