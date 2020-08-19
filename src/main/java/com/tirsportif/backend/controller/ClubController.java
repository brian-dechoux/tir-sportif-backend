package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.*;
import com.tirsportif.backend.service.ClubService;
import com.tirsportif.backend.service.ShooterService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/clubs", produces = "application/json;charset=UTF-8")
public class ClubController {

    private final ClubService clubService;
    private final ShooterService shooterService;

    public ClubController(ClubService clubService, ShooterService shooterService) {
        this.clubService = clubService;
        this.shooterService = shooterService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("authorizedFor('ADMIN')")
    public GetClubResponse createClub(@Valid @RequestBody CreateClubRequest request) {
        return clubService.createClub(request);
    }

    @GetMapping(value = "/{clubId}")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetClubResponse getClub(@PathVariable Long clubId) {
        return clubService.getClubById(clubId);
    }

    @GetMapping(value = "/my")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetClubResponse getMyClub() {
        return clubService.getMyClub();
    }

    // TODO create a route returning a resume for a club
    //  Club, nb shooters, nb challenges

    @GetMapping("/search")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public Page<GetClubListElementResponse> searchClubs(@RequestParam("page") int page, @RequestParam("rowsPerPage") int rowsPerPage) {
        return clubService.searchClubs(page, rowsPerPage);
    }

    @GetMapping(value = "/{clubId}/shooters")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public Page<GetShooterListElementResponse> getShooters(@PathVariable Long clubId, @RequestParam("page") int page, @RequestParam("rowsPerPage") int rowsPerPage) {
        return shooterService.getShootersForClub(clubId, page, rowsPerPage);
    }

    @GetMapping
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public List<GetClubResponse> getClubs(@RequestParam(required = false, defaultValue = "false") boolean withMyClub) {
        return clubService.getClubs(withMyClub);
    }

    @PutMapping(value = "/{clubId}")
    @PreAuthorize("authorizedFor('ADMIN')")
    public GetClubResponse updateClub(@PathVariable Long clubId, @Valid @RequestBody UpdateClubRequest request) {
       return  clubService.updateClub(clubId, request);
    }

    @PostMapping(value = "/{clubId}/shooters/{shooterId}/associate")
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetShooterResponse associateShooter(@PathVariable Long clubId, @PathVariable Long shooterId) {
        return shooterService.associateShooter(shooterId, clubId);
    }

}
