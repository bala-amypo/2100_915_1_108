package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService,
                          JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {

        User savedUser = userService.save(user);

        // ✅ CORRECT PARAMETER ORDER
        String token = jwtTokenProvider.generateToken(
                savedUser.getEmail(),   // username
                savedUser.getRole(),    // role
                savedUser.getId()       // userId (Long)
        );

        return ResponseEntity.ok(token);
    }
}
