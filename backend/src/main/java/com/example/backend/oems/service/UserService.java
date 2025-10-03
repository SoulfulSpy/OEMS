package com.example.backend.oems.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.backend.oems.entity.User;
import com.example.backend.oems.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByPhone(String phone) { return userRepository.findByPhone(phone); }
    public Optional<User> findByEmail(String email) { return userRepository.findByEmail(email); }
    public Optional<User> findById(java.util.UUID id) { return userRepository.findById(id); }
    public User save(User user) { return userRepository.save(user); }
}


