package com.example.demo.service;

import com.example.demo.model.User;

import java.util.Optional;

public interface UserService {

    User registerUser(User user);

    Optional<User> findByEmail(String email);
}
