package com.tirsportif.backend.configuration;

import com.tirsportif.backend.model.User;
import com.tirsportif.backend.repository.UserRepository;
import com.tirsportif.backend.service.JwtTokenManager;
import io.jsonwebtoken.ExpiredJwtException;
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

    // TODO proper exception
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenManager.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String finalUsername = username;
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username: "+ finalUsername));
            if (jwtTokenManager.validateToken(jwtToken, user)) {
                SecurityContextHolder.getContext().setAuthentication(user);
            }
        }
        chain.doFilter(request, response);
    }
}
