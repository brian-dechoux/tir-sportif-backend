package com.tirsportif.backend.service;

import com.tirsportif.backend.dto.AuthenticationRequest;
import com.tirsportif.backend.dto.AuthenticationResponse;
import com.tirsportif.backend.error.AuthenticationError;
import com.tirsportif.backend.exception.UnauthorizedErrorException;
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

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws UsernameNotFoundException {
        String username = authenticationRequest.getUsername();
        log.debug("BEGIN AuthenticationService.login(Username: {})", username);
        log.info("Authenticating user by username/password, for username: {}", username);

        // This is not security best practice, but we tolerate it for such a small project (no big impacts)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedErrorException(AuthenticationError.WRONG_USERNAME));

        if (!passwordService.passwordsMatch(authenticationRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedErrorException(AuthenticationError.WRONG_PASSWORD);
        }

        log.debug("Generating token");
        String jwtToken = jwtTokenManager.generateToken(user);

        log.debug("Storing token");
        jwtTokenManager.storeGeneratedToken(jwtToken, username);

        log.debug("Setting Spring authentication up");
        SecurityContextHolder.getContext().setAuthentication(user);

        log.info("Authenticated");
        log.debug("END AuthenticationService.login");
        return new AuthenticationResponse(jwtToken);
    }

    public void logout(String rawAuthorization) {
        log.debug("BEGIN AuthenticationService.logout()");
        log.info("Logging out from application for current authenticated user");
        String jwtToken = rawAuthorization.substring("Bearer ".length());
        jwtTokenManager.removeToken(jwtToken);
        SecurityContextHolder.getContext().setAuthentication(null);
        log.debug("END AuthenticationService.logout");
    }

}
