package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    // ✅ REQUIRED NO-ARG CONSTRUCTOR (for tests)
    public CustomUserDetailsService() {
    }

    // ✅ EXISTING CONSTRUCTOR (Spring will use this)
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    // ✅ REQUIRED BY TESTS
    public Map<String, Object> registerUser(
            String fullName,
            String email,
            String password,
            String role) {

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("role", role);
        return response;
    }
}
