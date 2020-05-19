package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.service.ChallengeService;
import com.tirsportif.backend.service.ParticipationService;
import com.tirsportif.backend.service.ShotResultService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetChallengeResponse createChallenge(@Valid @RequestBody CreateChallengeRequest request) {
        return challengeService.createChallenge(request);
    }

    @GetMapping(value = "/{challengeId}")
    @PreAuthorize("authorizedFor('VIEW')")
    public GetChallengeResponse getChallenge(@PathVariable Long challengeId) {
        return challengeService.getChallenge(challengeId);
    }

    @PutMapping(value = "/{challengeId}")
    @PreAuthorize("authorizedFor('ADMIN')")
    public GetChallengeResponse updateChallenge(@PathVariable Long challengeId, @Valid @RequestBody UpdateChallengeRequest request) {
        return challengeService.updateChallenge(challengeId, request);
    }

    @GetMapping(value = "/{challengeId}/results/category/{categoryId}/discipline/{disciplineId}")
    @PreAuthorize("authorizedFor('VIEW')")
    public GetCategoryAndDisciplineResultsResponse getResultsForCategory(@PathVariable Long challengeId, @PathVariable Long categoryId, @PathVariable Long disciplineId) {
        return shotResultService.getResultsForCategory(challengeId, categoryId, disciplineId);
    }

    @GetMapping(value = "/{challengeId}/results/shooter/{shooterId}")
    @PreAuthorize("authorizedFor('VIEW')")
    public GetShooterResultsResponse getResultsForShooter(@PathVariable Long challengeId, @PathVariable Long shooterId) {
        return shotResultService.getResultsForShooter(challengeId, shooterId);
    }

    @GetMapping
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public Page<GetChallengeListElementResponse> getChallenges(@RequestParam("page") int page, @RequestParam("rowsPerPage") int rowsPerPage) {
        return challengeService.getChallenges(page, rowsPerPage);
    }

    @GetMapping(value = "/{challengeId}/participants")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public Page<GetParticipantResponse> getParticipants(@PathVariable Long challengeId, @RequestParam("page") int page, @RequestParam("rowsPerPage") int rowsPerPage) {
        return participationService.getParticipants(challengeId, page, rowsPerPage);
    }

    @GetMapping(value = "/{challengeId}/participants/{participantId}/participations")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetShooterParticipationsResponse getShooterParticipations(@PathVariable Long challengeId, @PathVariable Long participantId) {
        return participationService.getParticipations(challengeId, participantId);
    }

    // TODO get participations for a specific shooter

    @PostMapping(value = "/{challengeId}/participations")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("authorizedFor('MANAGER')")
    public void createParticipations(@PathVariable Long challengeId, @Valid @RequestBody CreateParticipationsRequest request) {
        participationService.createParticipations(challengeId, request);
    }

    @DeleteMapping(value = "/{challengeId}/participations/{participationId}")
    @PreAuthorize("authorizedFor('MANAGER')")
    public void deleteParticipation(@PathVariable Long challengeId, @PathVariable Long participationId) {
        participationService.deleteParticipation(challengeId, participationId);
    }

    @PostMapping(value = "/{challengeId}/participations/{participationId}/shot-result")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("authorizedFor('MANAGER')")
    public void addShotResult(@PathVariable Long challengeId, @PathVariable Long participationId, @Valid @RequestBody AddShotResultRequest request) {
        shotResultService.addShotResult(challengeId, participationId, request);
    }

}
