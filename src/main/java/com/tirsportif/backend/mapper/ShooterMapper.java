package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetShooterResponse;
import com.tirsportif.backend.dto.ResolvedCreateShooterRequest;
import com.tirsportif.backend.model.Shooter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ShooterMapper {

    private final AddressMapper addressMapper;
    private final CategoryMapper categoryMapper;
    private final ClubMapper clubMapper;

    public ShooterMapper(AddressMapper addressMapper, CategoryMapper categoryMapper, ClubMapper clubMapper) {
        this.addressMapper = addressMapper;
        this.categoryMapper = categoryMapper;
        this.clubMapper = clubMapper;
    }

    public Shooter mapCreateShooterDtoToShooter(ResolvedCreateShooterRequest request) {
        Shooter shooter = new Shooter();
        shooter.setLastname(request.getLastname());
        shooter.setFirstname(request.getFirstname());
        shooter.setBirthDate(request.getBirthdate());
        shooter.setClub(request.getClub());
        shooter.setCategory(request.getCategory());
        if (request.getAddress() != null) {
            shooter.setAddress(addressMapper.mapAddressDtoToAddress(request.getAddress()));
        }
        return shooter;
    }

    public GetShooterResponse mapShooterToResponse(Shooter shooter) {
        return new GetShooterResponse(
                shooter.getId(),
                shooter.getLastname(),
                shooter.getFirstname(),
                shooter.getBirthDate(),
                Optional.ofNullable(shooter.getAddress())
                        .map(addressMapper::mapAddressToDto)
                        .orElse(null),
                Optional.ofNullable(shooter.getClub())
                        .map(clubMapper::mapClubToResponse)
                        .orElse(null),
                categoryMapper.mapCategoryToResponse(shooter.getCategory())
        );
    }

}
