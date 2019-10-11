package com.tirsportif.backend.dto;

import com.tirsportif.backend.model.Shooter;
import lombok.Value;

@Value
public class ResolvedCreateLicenseeRequest {

    Integer badgeNumber;

    Integer lockerNumber;

    Shooter shooter;

    public static ResolvedCreateLicenseeRequest ofRawRequest(CreateLicenseeRequest request, Shooter resolvedShooter) {
        return new ResolvedCreateLicenseeRequest(
                request.getBadgeNumber(),
                request.getLockerNumber(),
                resolvedShooter
        );
    }

}
