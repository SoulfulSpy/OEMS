import { Location, RideOption, Trip, Driver } from "../contexts/rideStore";

interface BookRideRequest {
  pickupLocation: Location;
  destination: Location;
  rideOption: RideOption;
  paymentMethod: string;
  promoCode?: string;
}

const API_BASE = (import.meta as any).env?.VITE_API_BASE_URL || "http://localhost:8080/api";

export const bookRide = async (request: BookRideRequest): Promise<Trip> => {
  const phone = (window as any).currentUser?.phone;
  const res = await fetch(`${API_BASE}/rides/book`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...(phone ? { "X-User-Phone": phone } : {}) },
    body: JSON.stringify({
      pickup: request.pickupLocation,
      destination: request.destination,
      rideOption: request.rideOption,
      paymentMethod: request.paymentMethod,
      promoCode: request.promoCode,
      userPhone: (window as any).currentUser?.phone || "+10000000000",
      userName: (window as any).currentUser?.name,
      userEmail: (window as any).currentUser?.email,
    }),
  });
  if (!res.ok) throw new Error("Failed to book ride");
  const data = await res.json();
  const driver: Driver = {
    id: "driver_pending",
    name: "Searching driver...",
    rating: 0,
    profilePicture: "https://via.placeholder.com/64x64?text=DRV",
    vehicle: { make: "", model: "", color: "", licensePlate: "" },
    location: { ...request.pickupLocation },
    eta: 5,
  };
  const trip: Trip = {
    id: String(data.id),
    pickupLocation: request.pickupLocation,
    destination: request.destination,
    driver,
    rideOption: request.rideOption,
    status: data.status,
    price: request.rideOption.price,
    startTime: new Date(),
  };

  // Simulate status updates client-side for UX until realtime backend exists
  setTimeout(() => {
    trip.status = "driver_assigned";
    trip.driver = {
      ...driver,
      id: "driver_123",
      name: "Rajesh Kumar",
      rating: 4.8,
      profilePicture: "https://via.placeholder.com/64x64?text=RK",
      vehicle: { make: "Maruti", model: "Swift", color: "White", licensePlate: "WB 01 AB 1234" },
      eta: 3,
    } as Driver;
  }, 3000);

  setTimeout(() => {
    trip.status = "pickup";
  }, 8000);

  setTimeout(() => {
    trip.status = "in_progress";
  }, 12000);

  setTimeout(() => {
    trip.status = "completed";
    trip.endTime = new Date();
  }, 20000);

  return trip;
};

export const cancelRide = async (tripId: string): Promise<void> => {
  const phone = (window as any).currentUser?.phone;
  const res = await fetch(`${API_BASE}/rides/${tripId}/cancel`, {
    method: "POST",
    headers: { ...(phone ? { "X-User-Phone": phone } : {}) },
  });
  if (!res.ok) throw new Error("Failed to cancel ride");
};

export const getRideEstimate = async (
  pickup: Location,
  destination: Location
): Promise<RideOption[]> => {
  const phone = (window as any).currentUser?.phone;
  const res = await fetch(`${API_BASE}/rides/estimate`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...(phone ? { "X-User-Phone": phone } : {}) },
    body: JSON.stringify({ pickup, destination }),
  });
  if (!res.ok) throw new Error("Failed to fetch ride estimates");
  const options = await res.json();
  return options as RideOption[];
};
