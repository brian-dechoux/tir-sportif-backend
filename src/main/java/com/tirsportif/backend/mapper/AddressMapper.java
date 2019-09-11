package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.CreateAddressRequest;
import com.tirsportif.backend.dto.GetAddressResponse;
import com.tirsportif.backend.model.Address;
import com.tirsportif.backend.model.Country;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address mapAddressDtoToAddress(CreateAddressRequest dto, Country country) {
        Address address = new Address();
        address.setNumber(dto.getNumber());
        address.setStreet(dto.getStreet());
        address.setZip(dto.getZip());
        address.setCity(dto.getCity());
        address.setCountry(country);
        return address;
    }

    GetAddressResponse mapAddressToDto(Address address) {
        GetAddressResponse dto = new GetAddressResponse();
        dto.setNumber(address.getNumber());
        dto.setStreet(address.getStreet());
        dto.setZip(address.getZip());
        dto.setCity(address.getCity());
        dto.setCountryCode(address.getCountry().getCode());
        return dto;
    }

}
