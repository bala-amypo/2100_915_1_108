package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    // ✅ Constructor Injection (MANDATORY for tests)
    public AuthController(
            UserService userService,
            JwtTokenProvider jwtTokenProvider,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {

        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @Valid @RequestBody User user) {

        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Email already exists"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User registered successfully"));
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String token = jwtTokenProvider.generateToken(request.getUsername());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Login successful",
                        new AuthResponse(token)
                )
        );
    }
}
