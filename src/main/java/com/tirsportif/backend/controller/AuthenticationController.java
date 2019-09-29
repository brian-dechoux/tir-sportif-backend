package com.tirsportif.backend.controller;

import com.tirsportif.backend.dto.AuthenticationRequest;
import com.tirsportif.backend.dto.AuthenticationResponse;
import com.tirsportif.backend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/authentication", produces = "application/json; charset=UTF-8")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value = "/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.login(authenticationRequest);
    }

    @PostMapping(value = "/logout")
    public void logout(@RequestHeader("Authorization") String rawAuthorization) {
        authenticationService.logout(rawAuthorization);
    }

}
