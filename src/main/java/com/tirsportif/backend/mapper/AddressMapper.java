package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetAddressResponse;
import com.tirsportif.backend.dto.ResolvedCreateAddressRequest;
import com.tirsportif.backend.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address mapAddressDtoToAddress(ResolvedCreateAddressRequest dto) {
        Address address = new Address();
        address.setNumber(dto.getNumber());
        address.setStreet(dto.getStreet());
        address.setZip(dto.getZip());
        address.setCity(dto.getCity());
        address.setCountry(dto.getCountry());
        return address;
    }

    GetAddressResponse mapAddressToDto(Address address) {
        GetAddressResponse dto = new GetAddressResponse();
        dto.setNumber(address.getNumber());
        dto.setStreet(address.getStreet());
        dto.setZip(address.getZip());
        dto.setCity(address.getCity());
        dto.setCountryCode(address.getCountry().getCode());
        dto.setCountryName(address.getCountry().getName());
        return dto;
    }

}
