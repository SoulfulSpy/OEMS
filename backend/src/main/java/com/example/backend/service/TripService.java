package com.example.backend.service;

import com.example.backend.entity.Trip;
import com.example.backend.entity.User;
import com.example.backend.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {
    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip save(Trip trip) { return tripRepository.save(trip); }
    public Optional<Trip> findById(UUID id) { return tripRepository.findById(id); }
    public List<Trip> findByUser(User user) { return tripRepository.findByUserOrderByStartTimeDesc(user); }
}


