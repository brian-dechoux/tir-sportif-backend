package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetLicenseeListElementResponse;
import com.tirsportif.backend.dto.GetLicenseeResponse;
import com.tirsportif.backend.dto.ResolvedCreateLicenseeRequest;
import com.tirsportif.backend.model.Licensee;
import com.tirsportif.backend.model.projection.LicenseeListElementProjection;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LicenseeMapper {

    private final ShooterMapper shooterMapper;
    private final AddressMapper addressMapper;

    public LicenseeMapper(ShooterMapper shooterMapper, AddressMapper addressMapper) {
        this.shooterMapper = shooterMapper;
        this.addressMapper = addressMapper;
    }

    public Licensee mapCreateLicenseeDtoToLicensee(ResolvedCreateLicenseeRequest request, LocalDate subscriptionDate) {
        Licensee licensee = new Licensee();
        licensee.setBadgeNumber(request.getBadgeNumber());
        licensee.setLockerNumber(request.getLockerNumber());
        licensee.setSubscriptionDate(subscriptionDate);
        licensee.setShooter(request.getShooter());
        if (request.getAddress() != null) {
            licensee.setAddress(addressMapper.mapAddressDtoToAddress(request.getAddress()));
        }
        return licensee;
    }

    public GetLicenseeResponse mapLicenseeToResponse(Licensee licensee) {
        return new GetLicenseeResponse(
                licensee.getId(),
                licensee.getBadgeNumber(),
                licensee.getLockerNumber(),
                licensee.getSubscriptionDate(),
                shooterMapper.mapShooterToResponse(licensee.getShooter()),
                addressMapper.mapAddressToDto(licensee.getAddress())
        );
    }

    public GetLicenseeListElementResponse mapLicenseeListElementToResponse(LicenseeListElementProjection projection) {
        return new GetLicenseeListElementResponse(
                projection.getId(),
                projection.getLastname(),
                projection.getFirstname(),
                projection.getSubscriptionDate()
        );
    }

}
