package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetCountryResponse;
import com.tirsportif.backend.model.Country;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {

    public GetCountryResponse mapCountryToResponse(Country country) {
        return new GetCountryResponse(
                country.getId(),
                country.getCode(),
                country.getName()
        );
    }

}
