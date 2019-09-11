package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.model.Country;
import com.tirsportif.backend.model.Shooter;
import org.springframework.stereotype.Component;

@Component
public class ShooterMapper {

    private final AddressMapper addressMapper;
    private final ClubMapper clubMapper;

    public ShooterMapper(AddressMapper addressMapper, ClubMapper clubMapper) {
        this.addressMapper = addressMapper;
        this.clubMapper = clubMapper;
    }

    public Shooter mapCreateShooterDtoToShooter(ResolvedCreateShooterRequest request, Country country) {
        Shooter shooter = new Shooter();
        shooter.setLastname(request.getLastname());
        shooter.setFirstname(request.getFirstname());
        shooter.setBirthDate(request.getBirthdate());
        shooter.setAddress(addressMapper.mapAddressDtoToAddress(request.getAddress(), country));
        shooter.setClub(request.getClub());
        shooter.setCategory(request.getCategory());
        return shooter;
    }

    public GetShooterResponse mapShooterToResponse(Shooter shooter) {
        return new GetShooterResponse(
                shooter.getLastname(),
                shooter.getFirstname(),
                shooter.getBirthDate(),
                addressMapper.mapAddressToDto(shooter.getAddress()),
                clubMapper.mapClubToResponse(shooter.getClub()),
                shooter.getCategory()
        );
    }

}
