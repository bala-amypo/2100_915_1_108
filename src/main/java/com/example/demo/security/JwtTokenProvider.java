package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "demo-secret-key-123456";
    private static final long EXPIRATION_TIME = 3600000; // 1 hour

    // ================== TOKEN CREATION ==================
    public String generateToken(String username, Long userId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // ================== REQUIRED BY FILTER ==================
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            return !getClaimsFromToken(token)
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // ================== REQUIRED BY TEST CASES ==================
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Claims getAllClaims(String token) {
        return getClaimsFromToken(token);
    }

    public Long getUserIdFromToken(String token) {
        Object id = getClaimsFromToken(token).get("userId");
        return Long.parseLong(id.toString());
    }
}
