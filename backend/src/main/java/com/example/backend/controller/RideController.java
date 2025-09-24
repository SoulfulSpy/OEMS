package com.example.backend.controller;

import com.example.backend.entity.Trip;
import com.example.backend.entity.User;
import com.example.backend.service.TripService;
import com.example.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/rides")
public class RideController {
    private final TripService tripService;
    private final UserService userService;

    public RideController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    @PostMapping("/estimate")
    public ResponseEntity<List<Map<String, Object>>> estimate(@RequestBody EstimateRequest req) {
        List<Map<String, Object>> options = List.of(
                option("oems-go", "OEMS Go", "Hatchback", 120, 3, "🚗", 4),
                option("oems-sedan", "OEMS Sedan", "Sedan", 180, 5, "🚙", 4),
                option("oems-suv", "OEMS SUV", "SUV", 250, 7, "🚐", 6)
        );
        return ResponseEntity.ok(options);
    }

    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> book(@RequestBody BookRequest req) {
        User user = userService.findByPhone(req.userPhone())
                .orElseGet(() -> {
                    User u = new User();
                    u.setPhone(req.userPhone());
                    u.setName(Optional.ofNullable(req.userName()).orElse("User"));
                    u.setEmail(Optional.ofNullable(req.userEmail()).orElse("user@example.com"));
                    return userService.save(u);
                });

        Trip trip = new Trip();
        trip.setUser(user);
        trip.setPickupAddress(req.pickup().address());
        trip.setPickupLatitude(req.pickup().latitude());
        trip.setPickupLongitude(req.pickup().longitude());
        trip.setDestinationAddress(req.destination().address());
        trip.setDestinationLatitude(req.destination().latitude());
        trip.setDestinationLongitude(req.destination().longitude());
        trip.setRideOptionId(req.rideOption().id());
        trip.setRideOptionName(req.rideOption().name());
        trip.setPrice(BigDecimal.valueOf(req.rideOption().price()));
        trip.setStatus("searching");
        trip.setStartTime(Instant.now());
        trip = tripService.save(trip);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", trip.getId());
        response.put("status", trip.getStatus());
        response.put("price", trip.getPrice());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{tripId}/cancel")
    public ResponseEntity<?> cancel(@PathVariable UUID tripId) {
        Optional<Trip> t = tripService.findById(tripId);
        if (t.isEmpty()) return ResponseEntity.notFound().build();
        Trip trip = t.get();
        trip.setStatus("cancelled");
        trip.setEndTime(Instant.now());
        tripService.save(trip);
        return ResponseEntity.ok(Map.of("status", "cancelled"));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<?> getStatus(@PathVariable UUID tripId) {
        return tripService.findById(tripId)
                .<ResponseEntity<?>>map(trip -> ResponseEntity.ok(Map.of(
                        "id", trip.getId(),
                        "status", trip.getStatus(),
                        "price", trip.getPrice(),
                        "startTime", trip.getStartTime(),
                        "endTime", trip.getEndTime()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    private Map<String, Object> option(String id, String name, String type, int price, int eta, String icon, int capacity) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", id);
        m.put("name", name);
        m.put("type", type);
        m.put("price", price);
        m.put("eta", eta);
        m.put("icon", icon);
        m.put("capacity", capacity);
        return m;
    }

    public record EstimateRequest(Location pickup, Location destination) {}
    public record BookRequest(Location pickup, Location destination, RideOption rideOption,
                              String paymentMethod, String promoCode,
                              String userPhone, String userName, String userEmail) {}
    public record RideOption(String id, String name, String type,
                             Integer price, Integer eta, String icon,
                             Integer capacity) {}
    public record Location(Double latitude, Double longitude, String address) {}
}


