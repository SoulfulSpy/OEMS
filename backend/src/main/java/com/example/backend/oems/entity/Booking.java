package com.example.backend.oems.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Booking entity for managing ride bookings
 * Based on the Blue Tables section of the ERD
 */
@Entity
@Table(name = "bookings", indexes = {
    @Index(name = "idx_bookings_customer_id", columnList = "customerId"),
    @Index(name = "idx_bookings_driver_id", columnList = "driverId"),
    @Index(name = "idx_bookings_status", columnList = "status"),
    @Index(name = "idx_bookings_created_at", columnList = "createdAt"),
    @Index(name = "idx_bookings_pickup_location", columnList = "pickupLatitude, pickupLongitude")
})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "driver_id")
    private UUID driverId;

    @Column(name = "vehicle_id")
    private UUID vehicleId;

    @Column(name = "pickup_address", nullable = false, length = 500)
    private String pickupAddress;

    @Column(name = "pickup_latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal pickupLatitude;

    @Column(name = "pickup_longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal pickupLongitude;

    @Column(name = "destination_address", nullable = false, length = 500)
    private String destinationAddress;

    @Column(name = "destination_latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal destinationLatitude;

    @Column(name = "destination_longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal destinationLongitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType requestedVehicleType;

    @Column(name = "estimated_fare", precision = 10, scale = 2)
    private BigDecimal estimatedFare;

    @Column(name = "estimated_distance", precision = 8, scale = 2)
    private BigDecimal estimatedDistance; // in kilometers

    @Column(name = "estimated_duration")
    private Integer estimatedDuration; // in minutes

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(name = "scheduled_time")
    private Instant scheduledTime;

    @Column(name = "pickup_time")
    private Instant pickupTime;

    @Column(name = "special_instructions", length = 1000)
    private String specialInstructions;

    @Column(name = "passenger_count")
    private Integer passengerCount = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    // Constructors
    public Booking() {}

    public Booking(UUID customerId, String pickupAddress, BigDecimal pickupLatitude, 
                   BigDecimal pickupLongitude, String destinationAddress, 
                   BigDecimal destinationLatitude, BigDecimal destinationLongitude, 
                   VehicleType requestedVehicleType) {
        this.customerId = customerId;
        this.pickupAddress = pickupAddress;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.destinationAddress = destinationAddress;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.requestedVehicleType = requestedVehicleType;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public UUID getDriverId() { return driverId; }
    public void setDriverId(UUID driverId) { this.driverId = driverId; }

    public UUID getVehicleId() { return vehicleId; }
    public void setVehicleId(UUID vehicleId) { this.vehicleId = vehicleId; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public BigDecimal getPickupLatitude() { return pickupLatitude; }
    public void setPickupLatitude(BigDecimal pickupLatitude) { this.pickupLatitude = pickupLatitude; }

    public BigDecimal getPickupLongitude() { return pickupLongitude; }
    public void setPickupLongitude(BigDecimal pickupLongitude) { this.pickupLongitude = pickupLongitude; }

    public String getDestinationAddress() { return destinationAddress; }
    public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }

    public BigDecimal getDestinationLatitude() { return destinationLatitude; }
    public void setDestinationLatitude(BigDecimal destinationLatitude) { this.destinationLatitude = destinationLatitude; }

    public BigDecimal getDestinationLongitude() { return destinationLongitude; }
    public void setDestinationLongitude(BigDecimal destinationLongitude) { this.destinationLongitude = destinationLongitude; }

    public VehicleType getRequestedVehicleType() { return requestedVehicleType; }
    public void setRequestedVehicleType(VehicleType requestedVehicleType) { this.requestedVehicleType = requestedVehicleType; }

    public BigDecimal getEstimatedFare() { return estimatedFare; }
    public void setEstimatedFare(BigDecimal estimatedFare) { this.estimatedFare = estimatedFare; }

    public BigDecimal getEstimatedDistance() { return estimatedDistance; }
    public void setEstimatedDistance(BigDecimal estimatedDistance) { this.estimatedDistance = estimatedDistance; }

    public Integer getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(Integer estimatedDuration) { this.estimatedDuration = estimatedDuration; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public Instant getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(Instant scheduledTime) { this.scheduledTime = scheduledTime; }

    public Instant getPickupTime() { return pickupTime; }
    public void setPickupTime(Instant pickupTime) { this.pickupTime = pickupTime; }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }

    public Integer getPassengerCount() { return passengerCount; }
    public void setPassengerCount(Integer passengerCount) { this.passengerCount = passengerCount; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public void assignDriver(UUID driverId, UUID vehicleId) {
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.status = BookingStatus.DRIVER_ASSIGNED;
    }

    public void startTrip() {
        this.status = BookingStatus.IN_PROGRESS;
        this.pickupTime = Instant.now();
    }

    public void cancel(String reason) {
        this.status = BookingStatus.CANCELLED;
        this.specialInstructions = (specialInstructions != null ? specialInstructions + "; " : "") + 
                                  "Cancelled: " + reason;
    }

    public boolean isActive() {
        return status == BookingStatus.PENDING || status == BookingStatus.DRIVER_ASSIGNED || 
               status == BookingStatus.IN_PROGRESS;
    }

    public boolean isScheduled() {
        return scheduledTime != null && scheduledTime.isAfter(Instant.now());
    }

    // JPA Callbacks
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Vehicle types for booking requests
     */
    public enum VehicleType {
        SEDAN,
        HATCHBACK,
        SUV,
        AUTO_RICKSHAW,
        BIKE,
        LUXURY,
        PREMIUM
    }

    /**
     * Booking status lifecycle
     */
    public enum BookingStatus {
        PENDING,
        DRIVER_ASSIGNED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        NO_DRIVER_AVAILABLE
    }
}