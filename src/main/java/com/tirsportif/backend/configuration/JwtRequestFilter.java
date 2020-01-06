package com.tirsportif.backend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tirsportif.backend.dto.ErrorResponse;
import com.tirsportif.backend.error.AuthenticationError;
import com.tirsportif.backend.error.SystemError;
import com.tirsportif.backend.exception.ErrorException;
import com.tirsportif.backend.exception.InternalServerErrorException;
import com.tirsportif.backend.exception.UnauthorizedErrorException;
import com.tirsportif.backend.model.User;
import com.tirsportif.backend.model.redis.JwtTokenKey;
import com.tirsportif.backend.repository.UserRepository;
import com.tirsportif.backend.service.JwtTokenManager;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Filter checking that the request contains a JWT token in the headers.
 * If yes, the current user is authenticated.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenManager jwtTokenManager;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public JwtRequestFilter(RedisTemplate<String, String> redisTemplate, JwtTokenManager jwtTokenManager, UserRepository userRepository, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.jwtTokenManager = jwtTokenManager;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String username, jwtToken;

        try {
            if (requestTokenHeader != null) {
                if (requestTokenHeader.startsWith("Bearer ")) {
                    jwtToken = requestTokenHeader.substring(7);
                    username = Optional.ofNullable(redisTemplate.opsForValue().get(new JwtTokenKey(jwtToken).getFormattedKey()))
                            .orElseThrow(() -> new UnauthorizedErrorException(AuthenticationError.UNKNOWN_TOKEN));
                } else {
                    throw new UnauthorizedErrorException(AuthenticationError.WRONG_FORMAT_TOKEN);
                }

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new InternalServerErrorException(SystemError.TECHNICAL_ERROR, "Cannot find user with username: " + username));
                    if (jwtTokenManager.validateToken(jwtToken, user)) {
                        SecurityContextHolder.getContext().setAuthentication(user);
                    }
                }
            }

            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            handleException(new UnauthorizedErrorException(AuthenticationError.EXPIRED_TOKEN), HttpServletResponse.SC_UNAUTHORIZED, response);
        } catch (UnauthorizedErrorException e) {
            handleException(e, HttpServletResponse.SC_UNAUTHORIZED, response);
        } catch (ErrorException e) {
            handleException(e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
        } catch (Exception e) {
            handleException(new InternalServerErrorException(SystemError.TECHNICAL_ERROR, e.getMessage()), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
        }
    }

    private void handleException(ErrorException e, int httpErrorCode, HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());
        String error = objectMapper.writeValueAsString(errorResponse);
        response.setStatus(httpErrorCode);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(error);
    }
}
