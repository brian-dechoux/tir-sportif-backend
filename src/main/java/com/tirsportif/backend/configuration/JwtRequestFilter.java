package com.tirsportif.backend.configuration;

import com.tirsportif.backend.error.AuthenticationError;
import com.tirsportif.backend.error.SystemError;
import com.tirsportif.backend.exception.InternalServerErrorException;
import com.tirsportif.backend.exception.UnauthorizedException;
import com.tirsportif.backend.model.User;
import com.tirsportif.backend.repository.UserRepository;
import com.tirsportif.backend.service.JwtTokenManager;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter checking that the request contains a JWT token in the headers.
 * If yes, the current user is authenticated.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;
    private final UserRepository userRepository;

    public JwtRequestFilter(JwtTokenManager jwtTokenManager, UserRepository userRepository) {
        this.jwtTokenManager = jwtTokenManager;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String username, jwtToken;

        if (requestTokenHeader != null) {
            if (requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    // TODO Check in Redis, for a proper logout workflow
                    username = jwtTokenManager.getUsernameFromToken(jwtToken);
                } catch (ExpiredJwtException e) {
                    throw new UnauthorizedException(AuthenticationError.EXPIRED_TOKEN);
                }
            } else {
                throw new UnauthorizedException(AuthenticationError.WRONG_FORMAT_TOKEN);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new InternalServerErrorException(SystemError.TECHNICAL_ERROR, "Cannot find user with username: "+ username));
                if (jwtTokenManager.validateToken(jwtToken, user)) {
                    SecurityContextHolder.getContext().setAuthentication(user);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
