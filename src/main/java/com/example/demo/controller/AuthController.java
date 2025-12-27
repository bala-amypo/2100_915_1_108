package com.example.demo.controller;

import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          CustomUserDetailsService customUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    // ✅ Register user
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role) {

        Map<String, Object> user =
                customUserDetailsService.registerUser(name, email, password, role);

        return ResponseEntity.ok(user);
    }

    // ✅ Login & generate JWT
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam String email,
            @RequestParam String password) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password)
                );

        Map<String, Object> user =
                customUserDetailsService.getUserByEmail(email);

        String token =
                jwtTokenProvider.generateToken(
                        authentication,
                        (Long) user.get("userId"),
                        (String) user.get("role")
                );

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.get("userId"));
        response.put("role", user.get("role"));
        response.put("email", email);

        return ResponseEntity.ok(response);
    }
}
