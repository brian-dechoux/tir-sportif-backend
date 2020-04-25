package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.GetCountryResponse;
import com.tirsportif.backend.service.CountryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/countries", produces = "application/json;charset=UTF-8")
public class CountriesController {

    private final CountryService countryService;

    public CountriesController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public List<GetCountryResponse> getCountries() {
        return countryService.getCountries();
    }

}
