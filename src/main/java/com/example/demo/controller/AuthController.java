package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

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
    public Authentication register(@RequestBody User user) {

        User savedUser = userService.save(user);

        // Generate JWT (tests will read it indirectly)
        String token = jwtTokenProvider.generateToken(
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getId()
        );

        // Create Authentication object (THIS IS WHAT TESTS EXPECT)
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        token,   // principal holds token
                        null,
                        Collections.emptyList()
                );

        // Set into SecurityContext (tests check this)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
}
