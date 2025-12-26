package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String jwtSecret;
    private final long jwtExpirationMs;
    private final boolean enabled;

    // ✅ REQUIRED BY TESTS
    public JwtTokenProvider(String jwtSecret, long jwtExpirationMs, boolean enabled) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
        this.enabled = enabled;
    }

    // ✅ DEFAULT CONSTRUCTOR FOR SPRING
    public JwtTokenProvider() {
        this.jwtSecret = "defaultSecretKey";
        this.jwtExpirationMs = 86400000; // 1 day
        this.enabled = true;
    }

    // ✅ REQUIRED BY TESTS
    public String generateToken(Authentication authentication, Long userId, String role) {

        String username = authentication.getName();

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    // ✅ USED BY FILTER
    public String generateToken(String username, Long userId) {

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    // ✅ REQUIRED BY TESTS
    public String getUsernameFromToken(String token) {
        return getAllClaims(token).getSubject();
    }

    // ✅ REQUIRED BY TESTS
    public Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            getAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
