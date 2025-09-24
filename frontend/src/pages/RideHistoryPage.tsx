import React, { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { ArrowLeft, Calendar, Star, MoreHorizontal } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { Trip } from "../contexts/rideStore";
import { formatDate, formatCurrency } from "../utils/formatters";
import Button from "../components/Button";

interface HistoryTrip extends Omit<Trip, "startTime" | "endTime"> {
  startTime: string;
  endTime: string;
  rating?: number;
}

const RideHistoryPage: React.FC = () => {
  const [trips, setTrips] = useState<HistoryTrip[]>([]);
  const [selectedTrip, setSelectedTrip] = useState<HistoryTrip | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    // Mock trip history data
    const mockTrips: HistoryTrip[] = [
      {
        id: "1",
        pickupLocation: {
          latitude: 22.4734,
          longitude: 88.4263,
          address: "Rajpur Sonarpur, West Bengal",
        },
        destination: {
          latitude: 22.5726,
          longitude: 88.3639,
          address: "Park Street, Kolkata, West Bengal",
        },
        driver: {
          id: "driver1",
          name: "Rajesh Kumar",
          rating: 4.8,
          profilePicture: "https://via.placeholder.com/64x64?text=RK",
          vehicle: {
            make: "Maruti",
            model: "Swift",
            color: "White",
            licensePlate: "WB 01 AB 1234",
          },
          location: { latitude: 22.4734, longitude: 88.4263, address: "" },
          eta: 0,
        },
        rideOption: {
          id: "oems-go",
          name: "OEMS Go",
          type: "Hatchback",
          price: 120,
          eta: 0,
          icon: "🚗",
          capacity: 4,
        },
        status: "completed",
        price: 145,
        startTime: "2024-01-15T14:30:00Z",
        endTime: "2024-01-15T15:15:00Z",
        rating: 5,
      },
      {
        id: "2",
        pickupLocation: {
          latitude: 22.5726,
          longitude: 88.3639,
          address: "Park Street, Kolkata, West Bengal",
        },
        destination: {
          latitude: 22.4734,
          longitude: 88.4263,
          address: "Rajpur Sonarpur, West Bengal",
        },
        driver: {
          id: "driver2",
          name: "Amit Singh",
          rating: 4.6,
          profilePicture: "https://via.placeholder.com/64x64?text=AS",
          vehicle: {
            make: "Honda",
            model: "City",
            color: "Silver",
            licensePlate: "WB 02 CD 5678",
          },
          location: { latitude: 22.4734, longitude: 88.4263, address: "" },
          eta: 0,
        },
        rideOption: {
          id: "oems-sedan",
          name: "OEMS Sedan",
          type: "Sedan",
          price: 180,
          eta: 0,
          icon: "🚙",
          capacity: 4,
        },
        status: "completed",
        price: 180,
        startTime: "2024-01-14T09:15:00Z",
        endTime: "2024-01-14T10:00:00Z",
        rating: 4,
      },
    ];

    setTrips(mockTrips);
  }, []);

  if (selectedTrip) {
    return (
      <div className="min-h-screen bg-gray-50">
        {/* Header */}
        <div className="glass-panel mx-4 mt-12 mb-6 p-4">
          <div className="flex items-center gap-4">
            <button
              onClick={() => setSelectedTrip(null)}
              className="p-2 rounded-full hover:bg-white/20"
            >
              <ArrowLeft className="w-6 h-6" />
            </button>
            <h1 className="text-lg font-semibold">Trip Details</h1>
          </div>
        </div>

        {/* Trip Details */}
        <div className="px-4 space-y-4">
          {/* Route */}
          <div className="glass-panel p-6">
            <div className="space-y-4">
              <div className="flex items-start gap-4">
                <div className="w-3 h-3 bg-green-500 rounded-full mt-2"></div>
                <div>
                  <p className="font-medium">Pickup</p>
                  <p className="text-sm text-gray-600">
                    {selectedTrip.pickupLocation.address}
                  </p>
                  <p className="text-xs text-gray-500">
                    {formatDate(selectedTrip.startTime)}
                  </p>
                </div>
              </div>

              <div className="ml-6 border-l-2 border-gray-200 h-8"></div>

              <div className="flex items-start gap-4">
                <div className="w-3 h-3 bg-red-500 rounded-full mt-2"></div>
                <div>
                  <p className="font-medium">Destination</p>
                  <p className="text-sm text-gray-600">
                    {selectedTrip.destination.address}
                  </p>
                  <p className="text-xs text-gray-500">
                    {formatDate(selectedTrip.endTime)}
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* Driver Info */}
          <div className="glass-panel p-6">
            <h3 className="font-semibold mb-4">Driver</h3>
            <div className="flex items-center gap-4">
              <div className="w-12 h-12 bg-gray-300 rounded-full overflow-hidden">
                <img
                  src={selectedTrip.driver.profilePicture}
                  alt={selectedTrip.driver.name}
                  className="w-full h-full object-cover"
                />
              </div>
              <div>
                <h4 className="font-medium">{selectedTrip.driver.name}</h4>
                <div className="flex items-center gap-2">
                  <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                  <span className="text-sm">{selectedTrip.driver.rating}</span>
                </div>
                <p className="text-sm text-gray-600">
                  {selectedTrip.driver.vehicle.color}{" "}
                  {selectedTrip.driver.vehicle.make}{" "}
                  {selectedTrip.driver.vehicle.model}
                </p>
              </div>
            </div>
          </div>

          {/* Fare Details */}
          <div className="glass-panel p-6">
            <h3 className="font-semibold mb-4">Fare details</h3>
            <div className="space-y-3">
              <div className="flex justify-between">
                <span>Base fare</span>
                <span>{formatCurrency(selectedTrip.price)}</span>
              </div>
              <div className="flex justify-between font-semibold text-lg border-t border-gray-200 pt-3">
                <span>Total</span>
                <span>{formatCurrency(selectedTrip.price)}</span>
              </div>
            </div>
          </div>

          {/* Rating */}
          {selectedTrip.rating && (
            <div className="glass-panel p-6">
              <h3 className="font-semibold mb-4">Your rating</h3>
              <div className="flex items-center gap-2">
                {[1, 2, 3, 4, 5].map((star) => (
                  <Star
                    key={star}
                    className={`w-6 h-6 ${
                      star <= selectedTrip.rating!
                        ? "fill-yellow-400 text-yellow-400"
                        : "text-gray-300"
                    }`}
                  />
                ))}
                <span className="ml-2 font-medium">
                  {selectedTrip.rating}.0
                </span>
              </div>
            </div>
          )}

          {/* Actions */}
          <div className="pb-8">
            <Button
              onClick={() => console.log("Book again")}
              className="w-full"
              size="lg"
            >
              Book Again
            </Button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="glass-panel mx-4 mt-12 mb-6 p-4">
        <div className="flex items-center gap-4">
          <button
            onClick={() => navigate("/")}
            className="p-2 rounded-full hover:bg-white/20"
          >
            <ArrowLeft className="w-6 h-6" />
          </button>
          <h1 className="text-lg font-semibold">Your trips</h1>
        </div>
      </div>

      {/* Trip List */}
      <div className="px-4 space-y-3">
        {trips.map((trip) => (
          <motion.button
            key={trip.id}
            onClick={() => setSelectedTrip(trip)}
            className="w-full glass-panel p-4 text-left"
            whileHover={{ scale: 1.02 }}
            whileTap={{ scale: 0.98 }}
          >
            <div className="flex items-center justify-between mb-3">
              <div className="flex items-center gap-2">
                <Calendar className="w-4 h-4 text-gray-500" />
                <span className="text-sm text-gray-600">
                  {formatDate(trip.startTime)}
                </span>
              </div>
              <div className="flex items-center gap-2">
                <span className="font-semibold">
                  {formatCurrency(trip.price)}
                </span>
                <MoreHorizontal className="w-4 h-4 text-gray-400" />
              </div>
            </div>

            <div className="space-y-2">
              <div className="flex items-center gap-3 text-sm">
                <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                <span className="text-gray-600 truncate">
                  {trip.pickupLocation.address}
                </span>
              </div>
              <div className="flex items-center gap-3 text-sm">
                <div className="w-2 h-2 bg-red-500 rounded-full"></div>
                <span className="text-gray-600 truncate">
                  {trip.destination.address}
                </span>
              </div>
            </div>

            <div className="flex items-center justify-between mt-3">
              <span className="text-sm text-gray-500">
                {trip.rideOption.name}
              </span>
              {trip.rating && (
                <div className="flex items-center gap-1">
                  <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                  <span className="text-sm">{trip.rating}</span>
                </div>
              )}
            </div>
          </motion.button>
        ))}
      </div>
    </div>
  );
};

export default RideHistoryPage;
