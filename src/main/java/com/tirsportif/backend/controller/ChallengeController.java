package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.service.ChallengeService;
import com.tirsportif.backend.service.ParticipationService;
import com.tirsportif.backend.service.ShotResultService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/challenges", produces = "application/json;charset=UTF-8")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final ParticipationService participationService;
    private final ShotResultService shotResultService;

    public ChallengeController(ChallengeService challengeService, ParticipationService participationService, ShotResultService shotResultService) {
        this.challengeService = challengeService;
        this.participationService = participationService;
        this.shotResultService = shotResultService;
    }

    @PostMapping
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetChallengeResponse createChallenge(@Valid @RequestBody CreateChallengeRequest request) {
        return challengeService.createChallenge(request);
    }

    @GetMapping(value = "/{challengeId}")
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetChallengeResponse getChallenge(@PathVariable Long challengeId) {
        return challengeService.getChallenge(challengeId);
    }

    @PutMapping(value = "/{challengeId}")
    @PreAuthorize("authorizedFor('ADMIN')")
    public GetChallengeResponse updateChallenge(@PathVariable Long challengeId, @Valid @RequestBody UpdateChallengeRequest request) {
        return challengeService.updateChallenge(challengeId, request);
    }

    @GetMapping
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public Page<GetChallengeResponse> getChallenges(@RequestParam("page") int page) {
        return challengeService.getChallenges(page);
    }

    @PostMapping(value = "/{challengeId}/participations")
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetParticipationsResponse createParticipations(@PathVariable Long challengeId, @Valid @RequestBody CreateParticipationsRequest request) {
        return participationService.createParticipations(challengeId, request);
    }

    @DeleteMapping(value = "/{challengeId}/participations/{participationId}")
    @PreAuthorize("authorizedFor('MANAGER')")
    public void deleteParticipation(@PathVariable Long challengeId, @PathVariable Long participationId) {
        participationService.deleteParticipation(challengeId, participationId);
    }

    @PostMapping(value = "/{challengeId}/participations/{participationId}/shot-result")
    @PreAuthorize("authorizedFor('MANAGER')")
    public void addShotResult(@PathVariable Long challengeId, @PathVariable Long participationId, @Valid @RequestBody AddShotResultRequest request) {
        shotResultService.addShotResult(challengeId, participationId, request);
    }

}
