package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.CreateClubRequest;
import com.tirsportif.backend.dto.GetClubListElementResponse;
import com.tirsportif.backend.dto.GetClubResponse;
import com.tirsportif.backend.dto.UpdateClubRequest;
import com.tirsportif.backend.service.ClubService;
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

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
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

    @GetMapping
    @ResponseBody
    @PreAuthorize("authorizedFor('MANAGER')")
    public List<GetClubResponse> getClubs() {
        return clubService.getClubs();
    }

    @PutMapping(value = "/{clubId}")
    @PreAuthorize("authorizedFor('ADMIN')")
    public GetClubResponse updateClub(@PathVariable Long clubId, @Valid @RequestBody UpdateClubRequest request) {
       return  clubService.updateClub(clubId, request);
    }

}
