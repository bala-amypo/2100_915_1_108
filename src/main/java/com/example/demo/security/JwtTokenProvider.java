package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtTokenProvider {

    private final Key key;
    private final long validityInMs;
    private final boolean enabled;

    public JwtTokenProvider(String secret, long validityInMs, boolean enabled) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.validityInMs = validityInMs;
        this.enabled = enabled;
    }

    // =========================
    // TOKEN GENERATION
    // =========================
    public String generateToken(String username, String role, Long userId) {

        if (!enabled) return null;

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        claims.put("userId", userId);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // =========================
    // METHODS REQUIRED BY TESTS
    // =========================
    public String getUsernameFromToken(String token) {
        return getAllClaims(token).getSubject();
    }

    public Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            getAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getRole(String token) {
        return getAllClaims(token).get("role", String.class);
    }

    public Long getUserId(String token) {
        return getAllClaims(token).get("userId", Long.class);
    }
}
