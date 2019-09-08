package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.AddressDto;
import com.tirsportif.backend.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    private final CountryMapper countryMapper;

    public AddressMapper(CountryMapper countryMapper) {
        this.countryMapper = countryMapper;
    }

    public Address mapAddressDtoToAddress(AddressDto dto) {
        Address address = new Address();
        address.setNumber(dto.getNumber());
        address.setStreet(dto.getStreet());
        address.setZip(dto.getZip());
        address.setCity(dto.getCity());
        address.setCountry(countryMapper.mapCountryDtoToCountry(dto.getCountry()));
        return address;
    }

    AddressDto mapAddressToDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setNumber(address.getNumber());
        dto.setStreet(address.getStreet());
        dto.setZip(address.getZip());
        dto.setCity(address.getCity());
        dto.setCountry(countryMapper.mapCountryToCountryDto(address.getCountry()));
        return dto;
    }

}
