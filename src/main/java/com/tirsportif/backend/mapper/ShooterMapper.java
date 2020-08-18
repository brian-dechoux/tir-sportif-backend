package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetSearchShooterResponse;
import com.tirsportif.backend.dto.GetShooterResponse;
import com.tirsportif.backend.dto.ResolvedCreateShooterRequest;
import com.tirsportif.backend.model.Shooter;
import com.tirsportif.backend.model.projection.SearchShooterProjection;
import com.tirsportif.backend.utils.NameNormalizer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ShooterMapper {

    private final CategoryMapper categoryMapper;
    private final ClubMapper clubMapper;

    public ShooterMapper(CategoryMapper categoryMapper, ClubMapper clubMapper) {
        this.categoryMapper = categoryMapper;
        this.clubMapper = clubMapper;
    }

    public Shooter mapCreateShooterDtoToShooter(ResolvedCreateShooterRequest request) {
        Shooter shooter = new Shooter();
        shooter.setLastname(NameNormalizer.normalize(request.getLastname()));
        shooter.setFirstname(NameNormalizer.normalize(request.getFirstname()));
        shooter.setBirthDate(request.getBirthdate());
        shooter.setClub(request.getClub());
        shooter.setCategory(request.getCategory());
        shooter.setEmail(request.getEmail());
        return shooter;
    }

    public GetShooterResponse mapShooterToResponse(Shooter shooter) {
        return new GetShooterResponse(
                shooter.getId(),
                shooter.getLastname(),
                shooter.getFirstname(),
                shooter.getBirthDate(),
                Optional.ofNullable(shooter.getClub())
                        .map(clubMapper::mapClubToResponse)
                        .orElse(null),
                categoryMapper.mapCategoryToResponse(shooter.getCategory()),
                shooter.getEmail()
        );
    }

    public GetSearchShooterResponse mapSearchShooterToResponse(SearchShooterProjection shooterProjection) {
        return new GetSearchShooterResponse(
                shooterProjection.getId(),
                shooterProjection.getLastname(),
                shooterProjection.getFirstname(),
                shooterProjection.getClubName(),
                shooterProjection.getCategoryLabel()
        );
    }

}
