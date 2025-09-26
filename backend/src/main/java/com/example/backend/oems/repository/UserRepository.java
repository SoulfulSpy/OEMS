package com.example.backend.oems.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.oems.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    
    // Keep legacy method for backward compatibility
    default Optional<User> findByPhone(String phone) {
        return findByPhoneNumber(phone);
    }
}


