package com.example.backend.repository;

import com.example.backend.entity.Trip;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {
    List<Trip> findByUserOrderByStartTimeDesc(User user);
}


