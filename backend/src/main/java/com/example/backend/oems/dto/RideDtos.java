package com.example.backend.oems.dto;

/**
 * Data Transfer Objects for Ride endpoints
 */
public class RideDtos {
    
    public static class Location {
        private Double latitude;
        private Double longitude;
        private String address;
        
        public Location() {}
        
        public Location(Double latitude, Double longitude, String address) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.address = address;
        }
        
        // Getters and setters
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
        
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }
    
    public static class RideOption {
        private String id;
        private String name;
        private String type;
        private Double price;
        private Integer estimatedArrival;
        private Integer capacity;
        
        public RideOption() {}
        
        public RideOption(String id, String name, String type, Double price, Integer estimatedArrival, Integer capacity) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.price = price;
            this.estimatedArrival = estimatedArrival;
            this.capacity = capacity;
        }
        
        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        
        public Integer getEstimatedArrival() { return estimatedArrival; }
        public void setEstimatedArrival(Integer estimatedArrival) { this.estimatedArrival = estimatedArrival; }
        
        public Integer getCapacity() { return capacity; }
        public void setCapacity(Integer capacity) { this.capacity = capacity; }
    }
    
    public static class EstimateRequest {
        private Location pickup;
        private Location destination;
        
        public EstimateRequest() {}
        
        public EstimateRequest(Location pickup, Location destination) {
            this.pickup = pickup;
            this.destination = destination;
        }
        
        public Location getPickup() { return pickup; }
        public void setPickup(Location pickup) { this.pickup = pickup; }
        
        public Location getDestination() { return destination; }
        public void setDestination(Location destination) { this.destination = destination; }
    }
    
    public static class BookRequest {
        private Location pickup;
        private Location destination;
        private RideOption rideOption;
        private String userPhone;
        
        public BookRequest() {}
        
        public BookRequest(Location pickup, Location destination, RideOption rideOption, String userPhone) {
            this.pickup = pickup;
            this.destination = destination;
            this.rideOption = rideOption;
            this.userPhone = userPhone;
        }
        
        public Location getPickup() { return pickup; }
        public void setPickup(Location pickup) { this.pickup = pickup; }
        
        public Location getDestination() { return destination; }
        public void setDestination(Location destination) { this.destination = destination; }
        
        public RideOption getRideOption() { return rideOption; }
        public void setRideOption(RideOption rideOption) { this.rideOption = rideOption; }
        
        public String getUserPhone() { return userPhone; }
        public void setUserPhone(String userPhone) { this.userPhone = userPhone; }
    }
}