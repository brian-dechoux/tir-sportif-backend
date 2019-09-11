package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.CreateClubRequest;
import com.tirsportif.backend.dto.GetClubResponse;
import com.tirsportif.backend.dto.UpdateClubRequest;
import com.tirsportif.backend.model.Club;
import com.tirsportif.backend.model.Country;
import org.springframework.stereotype.Component;

@Component
public class ClubMapper {

    private final AddressMapper addressMapper;

    public ClubMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public Club mapCreateClubDtoToClub(CreateClubRequest request, Country country) {
        Club club = new Club();
        club.setName(request.getName());
        club.setAddress(addressMapper.mapAddressDtoToAddress(request.getAddress(), country));
        return club;
    }

    public Club mapUpdateClubDtoToClub(UpdateClubRequest request, Country country) {
        Club club = new Club();
        club.setName(request.getName());
        club.setAddress(addressMapper.mapAddressDtoToAddress(request.getAddress(), country));
        return club;
    }

    public GetClubResponse mapClubToResponse(Club club) {
        return new GetClubResponse(club.getName(), addressMapper.mapAddressToDto(club.getAddress()));
    }

}
