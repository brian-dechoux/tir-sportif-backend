package com.tirsportif.backend.dto;

import com.tirsportif.backend.model.Country;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ResolvedUpdateClubRequest {

    String name;

    ResolvedCreateAddressRequest address;

    public static ResolvedUpdateClubRequest ofRawRequest(UpdateClubRequest request, Country resolvedCountry) {
        return new ResolvedUpdateClubRequest(
            request.getName(),
            ResolvedCreateAddressRequest.ofRawRequest(request.getAddress(), resolvedCountry)
        );
    }

}
