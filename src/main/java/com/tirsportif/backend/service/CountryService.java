package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.GetCountryResponse;
import com.tirsportif.backend.mapper.CountryMapper;
import com.tirsportif.backend.property.ApiProperties;
import com.tirsportif.backend.repository.CountryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class CountryService extends AbstractService {

    private final CountryMapper countryMapper;
    private final CountryRepository countryRepository;

    public CountryService(ApiProperties apiProperties, CountryMapper countryMapper, CountryRepository countryRepository) {
        super(apiProperties);
        this.countryMapper = countryMapper;
        this.countryRepository = countryRepository;
    }

    public List<GetCountryResponse> getCountries() {
        log.info("Looking for all countries");
        List<GetCountryResponse> countries = StreamSupport.stream(countryRepository.findAll().spliterator(), false)
                .map(countryMapper::mapCountryToResponse)
                .collect(Collectors.toList());
        log.info("Found {} countries", countries.size());
        return countries;
    }

}
