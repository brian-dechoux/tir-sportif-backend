package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.AuthenticationRequest;
import com.tirsportif.backend.dto.AuthenticationResponse;
import com.tirsportif.backend.model.User;
import com.tirsportif.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;
    private final PasswordService passwordService;

    public AuthenticationService(UserRepository userRepository, JwtTokenManager jwtTokenManager, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.passwordService = passwordService;
    }

    // TODO Proper exception
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws UsernameNotFoundException {
        String username = authenticationRequest.getUsername();
        log.debug("BEGIN AuthenticationService.loadUserByUsername(Username: {})", username);
        log.info("Authenticating user by username/password, for username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username: "+username));

        if (!passwordService.passwordsMatch(authenticationRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password provided");
        }

        log.info("Generating token");
        String jwtToken = jwtTokenManager.generateToken(user);

        log.info("Setting Spring authentication up");
        SecurityContextHolder.getContext().setAuthentication(user);

        log.debug("END AuthenticationService.loadUserByUsername");
        return new AuthenticationResponse(jwtToken);
    }

}
