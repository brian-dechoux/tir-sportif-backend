package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.CreateChallengeRequest;
import com.tirsportif.backend.dto.CreateParticipationsRequest;
import com.tirsportif.backend.dto.GetChallengeResponse;
import com.tirsportif.backend.dto.GetParticipationsResponse;
import com.tirsportif.backend.service.ChallengeService;
import com.tirsportif.backend.service.ParticipationService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/challenges", produces = "application/json;charset=UTF-8")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final ParticipationService participationService;

    public ChallengeController(ChallengeService challengeService, ParticipationService participationService) {
        this.challengeService = challengeService;
        this.participationService = participationService;
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

}
