package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.CreateLicenseeRequest;
import com.tirsportif.backend.dto.GetLicenseeListElementResponse;
import com.tirsportif.backend.dto.GetLicenseeResponse;
import com.tirsportif.backend.service.LicenseeService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/licensees", produces = "application/json;charset=UTF-8")
public class LicenseeController {

    private final LicenseeService licenseeService;

    public LicenseeController(LicenseeService licenseeService) {
        this.licenseeService = licenseeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetLicenseeResponse createLicensee(@Valid @RequestBody CreateLicenseeRequest request) {
        return licenseeService.createLicensee(request);
    }

    @GetMapping
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public Page<GetLicenseeListElementResponse> getLicensees(@RequestParam("page") int page, @RequestParam("rowsPerPage") int rowsPerPage) {
        return licenseeService.getLicensees(page, rowsPerPage);
    }

    @GetMapping(value = "/{licenseeId}")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetLicenseeResponse getLicensee(@PathVariable Long licenseeId) {
        return licenseeService.getLicenseeById(licenseeId);
    }

    @PostMapping(value = "/{licenseeId}/renew")
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetLicenseeResponse renewLicenseeSubscription(@PathVariable Long licenseeId) {
        return licenseeService.renewLicenseeSubscription(licenseeId);
    }

}
