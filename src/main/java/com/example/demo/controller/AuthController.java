package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ================= REGISTER =================
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {

        String email = request.getEmail();
        String password = passwordEncoder.encode(request.getPassword());
        String role = request.getRole() != null ? request.getRole() : "USER";

        userDetailsService.registerUser(email, email, password, role);

        return ResponseEntity.ok(
                Map.of(
                        "message", "User registered successfully",
                        "email", email,
                        "role", role
                )
        );
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Map<String, Object> user = userDetailsService.getUserByEmail(request.getEmail());

        String token = jwtTokenProvider.generateToken(
                authentication,
                (Long) user.get("userId"),
                (String) user.get("role")
        );

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
