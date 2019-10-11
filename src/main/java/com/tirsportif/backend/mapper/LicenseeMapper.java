package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.CreateLicenseeRequest;
import com.tirsportif.backend.dto.GetLicenseeResponse;
import com.tirsportif.backend.model.Licensee;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class LicenseeMapper {

    private final ShooterMapper shooterMapper;

    public LicenseeMapper(ShooterMapper shooterMapper) {
        this.shooterMapper = shooterMapper;
    }

    public Licensee mapCreateLicenseeDtoToLicensee(CreateLicenseeRequest request, OffsetDateTime subscriptionDate) {
        Licensee licensee = new Licensee();
        licensee.setBadgeNumber(request.getBadgeNumber());
        licensee.setLockerNumber(request.getLockerNumber());
        licensee.setSubscriptionDate(subscriptionDate);
        return licensee;
    }

    public GetLicenseeResponse mapLicenseeToResponse(Licensee licensee) {
        return new GetLicenseeResponse(
                licensee.getId(),
                licensee.getBadgeNumber(),
                licensee.getLockerNumber(),
                licensee.getSubscriptionDate(),
                shooterMapper.mapShooterToResponse(licensee.getShooter())
        );
    }

}
