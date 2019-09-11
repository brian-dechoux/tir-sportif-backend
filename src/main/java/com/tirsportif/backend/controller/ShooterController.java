package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.CreateShooterRequest;
import com.tirsportif.backend.dto.GetClubResponse;
import com.tirsportif.backend.dto.GetShooterResponse;
import com.tirsportif.backend.service.ShooterService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/shooters", produces = "application/json;charset=UTF-8")
public class ShooterController {

    private final ShooterService shooterService;

    public ShooterController(ShooterService shooterService) {
        this.shooterService = shooterService;
    }

    @PostMapping
    public void createShooter(@RequestBody CreateShooterRequest request) {
        shooterService.createShooter(request);
    }

    @GetMapping(value = "/{shooterId}")
    @ResponseBody
    public GetShooterResponse getShooter(@PathVariable Long shooterId) {
        return shooterService.getShooterById(shooterId);
    }

}
