package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.CreateLicenseeRequest;
import com.tirsportif.backend.dto.GetLicenseeResponse;
import com.tirsportif.backend.service.LicenseeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/licensees", produces = "application/json;charset=UTF-8")
public class LicenseeController {

    private final LicenseeService licenseeService;

    public LicenseeController(LicenseeService licenseeService) {
        this.licenseeService = licenseeService;
    }

    @PostMapping
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetLicenseeResponse createLicensee(@RequestBody CreateLicenseeRequest request) {
        // TODO club should be resolved automatically, because a licensee can only be created in scope of own club
        return licenseeService.createLicensee(request);
    }

    @GetMapping(value = "/{licenseeId}")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetLicenseeResponse getLicensee(@PathVariable Long shooterId) {
        return licenseeService.getLicenseeById(shooterId);
    }

}
