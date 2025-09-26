package com.example.backend.oems.controller;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.oems.entity.Trip;
import com.example.backend.oems.entity.User;
import com.example.backend.oems.service.TripService;
import com.example.backend.oems.service.UserService;

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
    public ResponseEntity<List<Map<String, Object>>> estimate(@RequestBody Map<String, Object> req) {
        // TODO: Replace with real fare estimation logic
        // - Integrate with Google Maps Distance Matrix API for accurate distance/time calculation
        // - Implement dynamic pricing based on demand and supply
        // - Consider traffic conditions, peak hours, and surge pricing
        // - Validate pickup and destination coordinates
        // - Add support for different service types (economy, premium, luxury)
        List<Map<String, Object>> options = List.of(
                option("oems-go", "OEMS Go", "Hatchback", 120, 3, "🚗", 4),
                option("oems-sedan", "OEMS Sedan", "Sedan", 180, 5, "🚙", 4),
                option("oems-suv", "OEMS SUV", "SUV", 250, 7, "🚐", 6)
        );
        return ResponseEntity.ok(options);
    }

    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> book(@RequestBody Map<String, Object> req) {
        // Extract data from Map
        @SuppressWarnings("unchecked")
        Map<String, Object> pickup = (Map<String, Object>) req.get("pickup");
        @SuppressWarnings("unchecked")
        Map<String, Object> destination = (Map<String, Object>) req.get("destination");
        @SuppressWarnings("unchecked")
        Map<String, Object> rideOption = (Map<String, Object>) req.get("rideOption");
        String userPhone = (String) req.get("userPhone");
        
        // TODO: Replace auto-user creation with proper authentication validation
        // - Validate user is authenticated before allowing ride booking
        // - Use JWT token validation instead of phone number lookup
        // - Ensure user has completed profile verification
        // - Check user account status (active, suspended, etc.)
        // - Implement proper user session management
        User user = userService.findByPhone(userPhone)
                .orElseGet(() -> {
                    User u = new User();
                    u.setPhoneNumber(userPhone);
                    u.setFullName(Optional.ofNullable((String) req.get("userName")).orElse("User"));
                    u.setEmail(Optional.ofNullable((String) req.get("userEmail")).orElse("user@example.com"));
                    return userService.save(u);
                });

        // Create booking first (new entity model)
        // For now, create a simplified trip record for backward compatibility
        Trip trip = new Trip();
        
        // Generate a booking ID for this trip
        UUID bookingId = UUID.randomUUID();
        trip.setBookingId(bookingId);
        
        trip.setCustomerId(user.getId());
        
        // TODO: Replace dummy driver/vehicle assignment with real driver matching logic
        // - Implement driver matching algorithm based on proximity and availability
        // - Query available drivers within radius using PostGIS spatial queries
        // - Consider driver ratings, vehicle type compatibility, and estimated arrival time
        // - Implement driver acceptance/rejection workflow with timeout
        // - Add real-time driver location tracking using Redis/MQTT
        trip.setDriverId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        trip.setVehicleId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        
        trip.setPickupAddress((String) pickup.get("address"));
        trip.setPickupLatitude(BigDecimal.valueOf((Double) pickup.get("latitude")));
        trip.setPickupLongitude(BigDecimal.valueOf((Double) pickup.get("longitude")));
        trip.setDestinationAddress((String) destination.get("address"));
        trip.setDestinationLatitude(BigDecimal.valueOf((Double) destination.get("latitude")));
        trip.setDestinationLongitude(BigDecimal.valueOf((Double) destination.get("longitude")));
        trip.setEstimatedFare(BigDecimal.valueOf((Integer) rideOption.get("price")));
        // TODO: Replace immediate trip start with proper booking workflow
        // - Initial status should be REQUESTED, not STARTED
        // - Implement driver acceptance workflow before changing to ACCEPTED
        // - Add DRIVER_ARRIVING status when driver is en route to pickup
        // - Only change to STARTED when driver arrives and customer enters vehicle
        // - Add proper state machine validation for status transitions
        // - Implement timeout handling for driver acceptance (e.g., 2 minutes)
        trip.setStatus(Trip.TripStatus.STARTED);
        trip.setStartedAt(Instant.now());
        trip = tripService.save(trip);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", trip.getId());
        response.put("status", trip.getStatus().toString());
        response.put("price", trip.getEstimatedFare());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{tripId}/cancel")
    public ResponseEntity<Object> cancel(@PathVariable UUID tripId) {
        Optional<Trip> t = tripService.findById(tripId);
        if (t.isEmpty()) return ResponseEntity.notFound().build();
        Trip trip = t.get();
        trip.cancelTrip(Trip.CancelledBy.CUSTOMER, "Cancelled by customer");
        tripService.save(trip);
        return ResponseEntity.ok(Map.of("status", "cancelled"));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Object> getStatus(@PathVariable UUID tripId) {
        try {
            Optional<Trip> tripOpt = tripService.findById(tripId);
            if (tripOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Trip trip = tripOpt.get();
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("id", trip.getId().toString());
            response.put("status", trip.getStatus().toString());
            response.put("price", trip.getEstimatedFare() != null ? trip.getEstimatedFare() : trip.getActualFare());
            
            if (trip.getStartedAt() != null) {
                response.put("startTime", trip.getStartedAt().toString());
            }
            if (trip.getEndedAt() != null) {
                response.put("endTime", trip.getEndedAt().toString());
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error retrieving trip status for ID " + tripId + ": " + e.getMessage());
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to retrieve trip status: " + e.getMessage()));
        }
    }

    // TODO: Replace hardcoded ride options with database-driven vehicle types
    // - Store vehicle types and their properties in database (VehicleType entity)
    // - Make pricing configurable based on city, time, and demand
    // - Add support for different service categories (economy, premium, luxury, ambulance)
    // - Implement regional pricing variations
    // - Add dynamic ETA calculation based on real traffic data
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


}


