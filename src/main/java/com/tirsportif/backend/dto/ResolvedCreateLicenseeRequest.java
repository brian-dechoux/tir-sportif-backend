package com.tirsportif.backend.dto;

import com.tirsportif.backend.model.Country;
import com.tirsportif.backend.model.Shooter;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.util.Optional;

@Value
public class ResolvedCreateLicenseeRequest {

    Integer badgeNumber;

    Integer lockerNumber;

    Shooter shooter;

    ResolvedCreateAddressRequest address;

    public static ResolvedCreateLicenseeRequest ofRawRequest(CreateLicenseeRequest request, Shooter resolvedShooter, @Nullable Country country) {
        return new ResolvedCreateLicenseeRequest(
                request.getBadgeNumber(),
                request.getLockerNumber(),
                resolvedShooter,
                Optional.ofNullable(request.getAddress())
                        .map(address -> ResolvedCreateAddressRequest.ofRawRequest(request.getAddress(), country))
                .orElse(null)
        );
    }

}
