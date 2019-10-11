package com.tirsportif.backend.dto;

import com.tirsportif.backend.model.Country;
import lombok.NonNull;
import lombok.Value;

@Value
public class ResolvedCreateAddressRequest {

    @NonNull
    String street;

    String number;

    String zip;

    @NonNull
    String city;

    @NonNull
    Country country;

    public static ResolvedCreateAddressRequest ofRawRequest(CreateAddressRequest request, Country resolvedCountry) {
        return new ResolvedCreateAddressRequest(
                request.getStreet(),
                request.getNumber(),
                request.getZip(),
                request.getCity(),
                resolvedCountry
        );
    }

}
