package com.tirsportif.backend.service;

import com.tirsportif.backend.model.JwtTokenRedis;
import com.tirsportif.backend.model.User;
import com.tirsportif.backend.property.JwtProperties;
import com.tirsportif.backend.repository.JwtRepository;
import com.tirsportif.backend.utils.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT Token generator and validator.
 * This class model comes from: https://dzone.com/articles/spring-boot-security-json-web-tokenjwt-hello-world
 */
@Component
public class JwtTokenManager {

    private final DateUtils dateUtils;
    private final JwtProperties jwtProperties;
    private final JwtRepository jwtRepository;

    public JwtTokenManager(Clock clock, JwtProperties jwtProperties, JwtRepository jwtRepository) {
        this.dateUtils = DateUtils.fromClock(clock);
        this.jwtProperties = jwtProperties;
        this.jwtRepository = jwtRepository;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public boolean validateToken(String token, User user) {
        final String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, user.getUsername());
    }

    void storeGeneratedToken(String token) {
        JwtTokenRedis jwtRedisModel = new JwtTokenRedis(token, jwtProperties.getValidity());
        jwtRepository.save(jwtRedisModel);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(dateUtils.now())
                .setExpiration(dateUtils.nowPlus(jwtProperties.getValidity()))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret()).compact();
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(dateUtils.now());
    }

}
