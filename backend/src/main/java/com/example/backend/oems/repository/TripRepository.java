package com.example.backend.oems.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.oems.entity.Trip;

public interface TripRepository extends JpaRepository<Trip, UUID> {
    List<Trip> findByCustomerIdOrderByStartedAtDesc(UUID customerId);
    List<Trip> findByDriverIdOrderByStartedAtDesc(UUID driverId);
    List<Trip> findByBookingId(UUID bookingId);
}


