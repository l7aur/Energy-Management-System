package com.l7aur.authenticationmicroservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.l7aur.authenticationmicroservice.model.util.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.expiration}")
    private Duration expiration;

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String username, Role role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role.toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration.toMillis()))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public boolean validate(String token, UserDetails userDetails) {
        return !isTokenExpired(token) && extractUsername(token).equals(userDetails.getUsername());
    }

    public String extractUsername(String token) {
        return JWT.decode(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        return JWT.decode(token).getExpiresAt().before(new Date());
    }
}
