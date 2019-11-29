package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.GetChallengeFinanceResponse;
import com.tirsportif.backend.dto.GetShooterFinanceResponse;
import com.tirsportif.backend.service.FinanceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/finances", produces = "application/json;charset=UTF-8")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping(value = "/challenges/{challengeId}")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetChallengeFinanceResponse getChallengeFinances(@PathVariable Long challengeId) {
        return financeService.getChallengeFinances(challengeId);
    }

    @GetMapping(value = "/shooters/{shooterId}")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetShooterFinanceResponse getShooterFinances(@PathVariable Long shooterId) {
        return financeService.getShooterFinances(shooterId);
    }

}
