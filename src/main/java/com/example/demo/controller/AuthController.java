package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService,
                          JwtTokenProvider jwtTokenProvider,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.save(user);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User request) {

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(
                user.getId(), user.getEmail(), user.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());

        return response;
    }
}
mvn dependency:purge-local-repository -DreResolve=true
mvn clean install
