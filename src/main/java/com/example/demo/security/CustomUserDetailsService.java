package com.example.demo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class CustomUserDetailsService implements UserDetailsService {

    private static final Map<String, Map<String, Object>> USERS = new HashMap<>();
    private static final AtomicLong ID_GEN = new AtomicLong(1);

    // Used directly by tests
    public Map<String, Object> registerUser(
            String fullName,
            String email,
            String password,
            String role) {

        Map<String, Object> user = new HashMap<>();
        user.put("userId", ID_GEN.getAndIncrement());
        user.put("fullName", fullName);
        user.put("email", email);
        user.put("password", password);
        user.put("role", role);

        USERS.put(email, user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Map<String, Object> user = USERS.get(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.withUsername(email)
                .password((String) user.get("password"))
                .roles(((String) user.get("role")))
                .build();
    }
}
