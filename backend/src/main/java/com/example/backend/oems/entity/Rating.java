package com.example.backend.oems.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Rating entity for managing trip ratings and feedback
 * Based on the Orange Tables section of the ERD
 */
@Entity
@Table(name = "ratings", indexes = {
    @Index(name = "idx_ratings_trip_id", columnList = "tripId", unique = true),
    @Index(name = "idx_ratings_customer_id", columnList = "customerId"),
    @Index(name = "idx_ratings_driver_id", columnList = "driverId"),
    @Index(name = "idx_ratings_rating_value", columnList = "customerRating"),
    @Index(name = "idx_ratings_created_at", columnList = "createdAt")
})
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "trip_id", nullable = false, unique = true)
    private UUID tripId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "driver_id", nullable = false)
    private UUID driverId;

    @Column(name = "customer_rating", nullable = false)
    private Integer customerRating; // 1-5 stars

    @Column(name = "driver_rating")
    private Integer driverRating; // 1-5 stars (driver rates customer)

    @Column(name = "customer_feedback", length = 1000)
    private String customerFeedback;

    @Column(name = "driver_feedback", length = 1000)
    private String driverFeedback;

    @Column(name = "customer_rated_at")
    private Instant customerRatedAt;

    @Column(name = "driver_rated_at")
    private Instant driverRatedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    // Constructors
    public Rating() {}

    public Rating(UUID tripId, UUID customerId, UUID driverId) {
        this.tripId = tripId;
        this.customerId = customerId;
        this.driverId = driverId;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTripId() { return tripId; }
    public void setTripId(UUID tripId) { this.tripId = tripId; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public UUID getDriverId() { return driverId; }
    public void setDriverId(UUID driverId) { this.driverId = driverId; }

    public Integer getCustomerRating() { return customerRating; }
    public void setCustomerRating(Integer customerRating) { this.customerRating = customerRating; }

    public Integer getDriverRating() { return driverRating; }
    public void setDriverRating(Integer driverRating) { this.driverRating = driverRating; }

    public String getCustomerFeedback() { return customerFeedback; }
    public void setCustomerFeedback(String customerFeedback) { this.customerFeedback = customerFeedback; }

    public String getDriverFeedback() { return driverFeedback; }
    public void setDriverFeedback(String driverFeedback) { this.driverFeedback = driverFeedback; }

    public Instant getCustomerRatedAt() { return customerRatedAt; }
    public void setCustomerRatedAt(Instant customerRatedAt) { this.customerRatedAt = customerRatedAt; }

    public Instant getDriverRatedAt() { return driverRatedAt; }
    public void setDriverRatedAt(Instant driverRatedAt) { this.driverRatedAt = driverRatedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public void rateByCustomer(Integer rating, String feedback) {
        this.customerRating = rating;
        this.customerFeedback = feedback;
        this.customerRatedAt = Instant.now();
    }

    public void rateByDriver(Integer rating, String feedback) {
        this.driverRating = rating;
        this.driverFeedback = feedback;
        this.driverRatedAt = Instant.now();
    }

    public boolean isCustomerRated() {
        return customerRating != null && customerRatedAt != null;
    }

    public boolean isDriverRated() {
        return driverRating != null && driverRatedAt != null;
    }

    public boolean isBothRated() {
        return isCustomerRated() && isDriverRated();
    }

    public boolean isValidRating(Integer rating) {
        return rating != null && rating >= 1 && rating <= 5;
    }

    // JPA Callbacks
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}