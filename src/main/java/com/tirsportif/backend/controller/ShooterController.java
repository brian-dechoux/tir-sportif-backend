package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.CreateShooterRequest;
import com.tirsportif.backend.dto.GetShooterResponse;
import com.tirsportif.backend.service.ShooterService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/shooters", produces = "application/json;charset=UTF-8")
public class ShooterController {

    private final ShooterService shooterService;

    public ShooterController(ShooterService shooterService) {
        this.shooterService = shooterService;
    }

    @PostMapping
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetShooterResponse createShooter(@RequestBody CreateShooterRequest request) {
        return shooterService.createShooter(request);
    }

    @GetMapping(value = "/{shooterId}")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetShooterResponse getShooter(@PathVariable Long shooterId) {
        return shooterService.getShooterById(shooterId);
    }

    // TODO change this route it's plain wrong
    @GetMapping(value = "/{shooterId}/associate/{clubId}")
    @PostMapping
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetShooterResponse associateShooter(@PathVariable Long shooterId, @PathVariable Long clubId) {
        return shooterService.associateShooter(shooterId, clubId);
    }

}
