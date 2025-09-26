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
 * Trip entity for managing ride execution and tracking
 * Based on the Orange Tables section of the ERD
 */
@Entity
@Table(name = "trips", indexes = {
    @Index(name = "idx_trips_booking_id", columnList = "bookingId", unique = true),
    @Index(name = "idx_trips_customer_id", columnList = "customerId"),
    @Index(name = "idx_trips_driver_id", columnList = "driverId"),
    @Index(name = "idx_trips_status", columnList = "status"),
    @Index(name = "idx_trips_started_at", columnList = "startedAt"),
    @Index(name = "idx_trips_ended_at", columnList = "endedAt")
})
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_id", nullable = false, unique = true)
    private UUID bookingId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "driver_id", nullable = false)
    private UUID driverId;

    @Column(name = "vehicle_id", nullable = false)
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

    @Column(name = "actual_pickup_latitude", precision = 10, scale = 8)
    private BigDecimal actualPickupLatitude;

    @Column(name = "actual_pickup_longitude", precision = 11, scale = 8)
    private BigDecimal actualPickupLongitude;

    @Column(name = "actual_destination_latitude", precision = 10, scale = 8)
    private BigDecimal actualDestinationLatitude;

    @Column(name = "actual_destination_longitude", precision = 11, scale = 8)
    private BigDecimal actualDestinationLongitude;

    @Column(name = "estimated_distance", precision = 8, scale = 2)
    private BigDecimal estimatedDistance; // in kilometers

    @Column(name = "actual_distance", precision = 8, scale = 2)
    private BigDecimal actualDistance; // in kilometers

    @Column(name = "estimated_duration")
    private Integer estimatedDuration; // in minutes

    @Column(name = "actual_duration")
    private Integer actualDuration; // in minutes

    @Column(name = "estimated_fare", precision = 10, scale = 2)
    private BigDecimal estimatedFare;

    @Column(name = "actual_fare", precision = 10, scale = 2)
    private BigDecimal actualFare;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status = TripStatus.STARTED;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt = Instant.now();

    @Column(name = "pickup_completed_at")
    private Instant pickupCompletedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "cancelled_by")
    private CancelledBy cancelledBy;

    @Column(name = "otp_code", length = 6)
    private String otpCode;

    @Column(name = "special_instructions", length = 1000)
    private String specialInstructions;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    // Constructors
    public Trip() {}

    public Trip(UUID bookingId, UUID customerId, UUID driverId, UUID vehicleId,
                String pickupAddress, BigDecimal pickupLatitude, BigDecimal pickupLongitude,
                String destinationAddress, BigDecimal destinationLatitude, BigDecimal destinationLongitude) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.pickupAddress = pickupAddress;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.destinationAddress = destinationAddress;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getBookingId() { return bookingId; }
    public void setBookingId(UUID bookingId) { this.bookingId = bookingId; }

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

    public BigDecimal getActualPickupLatitude() { return actualPickupLatitude; }
    public void setActualPickupLatitude(BigDecimal actualPickupLatitude) { this.actualPickupLatitude = actualPickupLatitude; }

    public BigDecimal getActualPickupLongitude() { return actualPickupLongitude; }
    public void setActualPickupLongitude(BigDecimal actualPickupLongitude) { this.actualPickupLongitude = actualPickupLongitude; }

    public BigDecimal getActualDestinationLatitude() { return actualDestinationLatitude; }
    public void setActualDestinationLatitude(BigDecimal actualDestinationLatitude) { this.actualDestinationLatitude = actualDestinationLatitude; }

    public BigDecimal getActualDestinationLongitude() { return actualDestinationLongitude; }
    public void setActualDestinationLongitude(BigDecimal actualDestinationLongitude) { this.actualDestinationLongitude = actualDestinationLongitude; }

    public BigDecimal getEstimatedDistance() { return estimatedDistance; }
    public void setEstimatedDistance(BigDecimal estimatedDistance) { this.estimatedDistance = estimatedDistance; }

    public BigDecimal getActualDistance() { return actualDistance; }
    public void setActualDistance(BigDecimal actualDistance) { this.actualDistance = actualDistance; }

    public Integer getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(Integer estimatedDuration) { this.estimatedDuration = estimatedDuration; }

    public Integer getActualDuration() { return actualDuration; }
    public void setActualDuration(Integer actualDuration) { this.actualDuration = actualDuration; }

    public BigDecimal getEstimatedFare() { return estimatedFare; }
    public void setEstimatedFare(BigDecimal estimatedFare) { this.estimatedFare = estimatedFare; }

    public BigDecimal getActualFare() { return actualFare; }
    public void setActualFare(BigDecimal actualFare) { this.actualFare = actualFare; }

    public TripStatus getStatus() { return status; }
    public void setStatus(TripStatus status) { this.status = status; }

    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }

    public Instant getPickupCompletedAt() { return pickupCompletedAt; }
    public void setPickupCompletedAt(Instant pickupCompletedAt) { this.pickupCompletedAt = pickupCompletedAt; }

    public Instant getEndedAt() { return endedAt; }
    public void setEndedAt(Instant endedAt) { this.endedAt = endedAt; }

    public Instant getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(Instant cancelledAt) { this.cancelledAt = cancelledAt; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public CancelledBy getCancelledBy() { return cancelledBy; }
    public void setCancelledBy(CancelledBy cancelledBy) { this.cancelledBy = cancelledBy; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public void completePickup(BigDecimal actualLat, BigDecimal actualLng) {
        this.status = TripStatus.IN_PROGRESS;
        this.pickupCompletedAt = Instant.now();
        this.actualPickupLatitude = actualLat;
        this.actualPickupLongitude = actualLng;
    }

    public void completeTrip(BigDecimal actualLat, BigDecimal actualLng, 
                           BigDecimal distance, BigDecimal fare) {
        this.status = TripStatus.COMPLETED;
        this.endedAt = Instant.now();
        this.actualDestinationLatitude = actualLat;
        this.actualDestinationLongitude = actualLng;
        this.actualDistance = distance;
        this.actualFare = fare;
        
        if (startedAt != null && endedAt != null) {
            this.actualDuration = (int) java.time.Duration.between(startedAt, endedAt).toMinutes();
        }
    }

    public void cancelTrip(CancelledBy cancelledBy, String reason) {
        this.status = TripStatus.CANCELLED;
        this.cancelledAt = Instant.now();
        this.cancelledBy = cancelledBy;
        this.cancellationReason = reason;
    }

    public boolean isActive() {
        return status == TripStatus.STARTED || status == TripStatus.IN_PROGRESS;
    }

    public boolean isCompleted() {
        return status == TripStatus.COMPLETED;
    }

    public Integer getTripDurationMinutes() {
        if (startedAt != null && endedAt != null) {
            return (int) java.time.Duration.between(startedAt, endedAt).toMinutes();
        }
        return actualDuration;
    }

    // JPA Callbacks
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Trip status lifecycle
     */
    public enum TripStatus {
        STARTED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    /**
     * Who cancelled the trip
     */
    public enum CancelledBy {
        CUSTOMER,
        DRIVER,
        SYSTEM
    }
}


