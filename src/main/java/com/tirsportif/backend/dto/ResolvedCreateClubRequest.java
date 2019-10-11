package com.tirsportif.backend.dto;

import com.tirsportif.backend.model.Country;
import lombok.NonNull;
import lombok.Value;

@Value
public class ResolvedCreateClubRequest {

    @NonNull
    String name;

    @NonNull
    ResolvedCreateAddressRequest address;

    public static ResolvedCreateClubRequest ofRawRequest(CreateClubRequest request, Country resolvedCountry) {
        return new ResolvedCreateClubRequest(
                request.getName(),
                ResolvedCreateAddressRequest.ofRawRequest(request.getAddress(), resolvedCountry)
        );
    }

}
