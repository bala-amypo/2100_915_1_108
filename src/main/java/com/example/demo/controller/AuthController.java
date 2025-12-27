package com.example.demo.controller;

import com.example.demo.security.JwtTokenProvider;
import com.example.demo.security.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final CustomUserDetailsService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(CustomUserDetailsService userService,
                          JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role) {

        Map<String, Object> user =
                userService.registerUser(fullName, email, password, role);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String email,
            @RequestParam String password) {

        Authentication auth =
                new UsernamePasswordAuthenticationToken(email, password);

        String token =
                jwtTokenProvider.generateToken(
                        auth,
                        1L,
                        "USER"
                );

        return ResponseEntity.ok(token);
    }
}
