package com.example.demo.service;

import com.example.demo.model.User;

public interface UserService {

    User createUser(User user);

    boolean existsByEmail(String email);
}
