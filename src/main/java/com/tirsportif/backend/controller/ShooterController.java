package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.CreateShooterRequest;
import com.tirsportif.backend.dto.GetSearchShooterResponse;
import com.tirsportif.backend.dto.GetShooterResponse;
import com.tirsportif.backend.service.ShooterService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/shooters", produces = "application/json;charset=UTF-8")
public class ShooterController {

    private final ShooterService shooterService;

    public ShooterController(ShooterService shooterService) {
        this.shooterService = shooterService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetShooterResponse createShooter(@Valid @RequestBody CreateShooterRequest request) {
        return shooterService.createShooter(request);
    }

    @GetMapping(value = "/search")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public List<GetSearchShooterResponse> searchShooters(
            @RequestParam String searchName,
            @RequestParam(required = false, defaultValue = "false") boolean freeClubOnly,
            @RequestParam(required = false) List<Long> categoryIds
    ) {
        return shooterService.searchShooters(searchName, freeClubOnly, categoryIds);
    }

    @GetMapping(value = "/{shooterId}")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetShooterResponse getShooter(@PathVariable Long shooterId) {
        return shooterService.getShooterById(shooterId);
    }

}
