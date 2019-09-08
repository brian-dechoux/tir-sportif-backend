package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.CountryDto;
import com.tirsportif.backend.model.Country;
import org.springframework.stereotype.Component;

@Component
class CountryMapper {

    Country mapCountryDtoToCountry(CountryDto dto) {
        Country country = new Country();
        country.setValue(dto.getValue());
        return country;
    }

    CountryDto mapCountryToCountryDto(Country country) {
        CountryDto dto = new CountryDto();
        dto.setValue(country.getValue());
        return dto;
    }

}
