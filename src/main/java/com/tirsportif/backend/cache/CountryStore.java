package com.tirsportif.backend.cache;

import com.tirsportif.backend.model.Country;
import com.tirsportif.backend.repository.CountryRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class CountryStore implements Store {

    private final Map<Long,Country> countriesCache;

    public CountryStore(CountryRepository countryRepository) {
        this.countriesCache = StreamSupport.stream(countryRepository.findAll().spliterator(),false)
                .collect(Collectors.toMap(Country::getId, Function.identity()));
    }

    public Optional<Country> getCountryById(Long id) {
        return Optional.ofNullable(countriesCache.get(id));
    }

}
