package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetClubListElementResponse;
import com.tirsportif.backend.dto.GetClubResponse;
import com.tirsportif.backend.dto.ResolvedCreateClubRequest;
import com.tirsportif.backend.dto.ResolvedUpdateClubRequest;
import com.tirsportif.backend.model.Club;
import com.tirsportif.backend.model.projection.ClubListElementProjection;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClubMapper {

    private final AddressMapper addressMapper;

    public ClubMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public Club mapCreateClubDtoToClub(ResolvedCreateClubRequest request) {
        Club club = new Club();
        club.setName(request.getName());
        club.setAddress(addressMapper.mapAddressDtoToAddress(request.getAddress()));
        return club;
    }

    public Club mapUpdateClubDtoToClub(Club existingClub, ResolvedUpdateClubRequest request) {
        return existingClub.toBuilder()
                .name(Optional.ofNullable(request.getName()).orElse(existingClub.getName()))
                .address(
                        Optional.ofNullable(request.getAddress())
                                .map(addressMapper::mapAddressDtoToAddress)
                                .orElse(existingClub.getAddress())
                ).build();
    }

    public GetClubResponse mapClubToResponse(Club club) {
        return new GetClubResponse(
                club.getId(),
                club.getName(),
                addressMapper.mapAddressToDto(club.getAddress())
        );
    }

    public GetClubListElementResponse mapClubListElementProjectionToResponse(ClubListElementProjection projection) {
        return new GetClubListElementResponse(
                projection.getId(),
                projection.getName(),
                projection.getCity(),
                projection.getNbShooters()
        );
    }
}
