package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.CreateClubRequest;
import com.tirsportif.backend.dto.GetClubResponse;
import com.tirsportif.backend.dto.UpdateClubRequest;
import com.tirsportif.backend.service.ClubService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/clubs", produces = "application/json;charset=UTF-8")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping
    @PreAuthorize("authorizedFor('ADMIN')")
    public void createClub(@RequestBody CreateClubRequest request) {
        clubService.createClub(request);
    }

    @GetMapping(value = "/{clubId}")
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public GetClubResponse getClub(@PathVariable Long clubId) {
        return clubService.getClubById(clubId);
    }

    @GetMapping
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public Page<GetClubResponse> getClubs(@RequestParam("page") int page) {
        // TODO Exclude current club (Briey) as it's MyClub. Depends on connected user.
        return clubService.getClubs(page);
    }

    @PutMapping(value = "/{clubId}")
    @PreAuthorize("authorizedFor('ADMIN')")
    public void updateClub(@PathVariable Long clubId, @RequestBody UpdateClubRequest request) {
        clubService.updateClub(clubId, request);
    }

}
