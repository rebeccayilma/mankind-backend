package com.mankind.mankindmatrixuserservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key;

    // Set to store revoked tokens (thread-safe)
    private final Set<String> revokedTokens = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Error extracting username from token", e);
        }
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            // Check if token is revoked
            if (isTokenRevoked(token)) {
                return false;
            }

            String username = extractUsername(token);

            // Check if token is expired
            Date expiration = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();

            boolean isExpired = expiration.before(new Date());

            return username.equals(userDetails.getUsername()) && !isExpired;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Adds a token to the revoked list
     * @param token The token to revoke
     */
    public void revokeToken(String token) {
        revokedTokens.add(token);
    }

    /**
     * Checks if a token is revoked
     * @param token The token to check
     * @return true if the token is revoked, false otherwise
     */
    public boolean isTokenRevoked(String token) {
        return revokedTokens.contains(token);
    }
}
