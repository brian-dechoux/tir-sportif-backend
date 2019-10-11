package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.CreateChallengeRequest;
import com.tirsportif.backend.service.ChallengeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/challenges", produces = "application/json;charset=UTF-8")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @PostMapping
    @PreAuthorize("authorizedFor('MANAGER')")
    public void createShooter(@Valid @RequestBody CreateChallengeRequest request) {
        challengeService.createChallenge(request);
    }


}
