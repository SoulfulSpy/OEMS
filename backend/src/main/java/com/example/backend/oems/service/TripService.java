package com.example.backend.oems.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.backend.oems.entity.Trip;
import com.example.backend.oems.repository.TripRepository;

@Service
public class TripService {
    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip save(Trip trip) { return tripRepository.save(trip); }
    public Optional<Trip> findById(UUID id) { return tripRepository.findById(id); }
    public List<Trip> findByCustomerId(UUID customerId) { return tripRepository.findByCustomerIdOrderByStartedAtDesc(customerId); }
    public List<Trip> findByDriverId(UUID driverId) { return tripRepository.findByDriverIdOrderByStartedAtDesc(driverId); }
    public Optional<Trip> findByBookingId(UUID bookingId) { 
        List<Trip> trips = tripRepository.findByBookingId(bookingId);
        return trips.isEmpty() ? Optional.empty() : Optional.of(trips.get(0));
    }
}


