import React, { useState } from "react";
import { Search, Home, Briefcase, ArrowRight } from "lucide-react";
import { useRideStore } from "../contexts/rideStore";
import DestinationSearch from "./DestinationSearch";
import { getRideEstimate } from "../services/rideService";

interface SearchBarProps {
  onMenuClick: () => void;
}

const SearchBar: React.FC<SearchBarProps> = ({}) => {
  const [showDestinationSearch, setShowDestinationSearch] = useState(false);
  const {
    destination,
    userLocation,
    setDestination,
    setPickupLocation,
    setAvailableRideOptions,
  } = useRideStore();

  const handleWhereToClick = () => {
    setShowDestinationSearch(true);
  };

  const handleSelectDestination = async (address: string) => {
    // Use userLocation as pickup if available
    if (userLocation) {
      setPickupLocation(userLocation);
    }
    // Simple mock geocode: offset from user location or default center
    const baseLat = userLocation?.latitude ?? 22.4734;
    const baseLng = userLocation?.longitude ?? 88.4263;
    const dest = {
      latitude: baseLat + 0.01,
      longitude: baseLng + 0.01,
      address,
    };
    setDestination(dest);
    setShowDestinationSearch(false);

    try {
      const options = await getRideEstimate(userLocation ?? dest, dest);
      setAvailableRideOptions(options);
    } catch (e) {
      console.error("Failed to fetch ride estimates", e);
      setAvailableRideOptions([]);
    }
  };

  return (
    <>
      <div className="space-y-3">
        {/* Destination Input */}
        {!destination ? (
          <button
            onClick={handleWhereToClick}
            className="w-full bg-gray-100 hover:bg-gray-200 border border-gray-200 rounded-lg p-3 text-left flex items-center gap-3 transition-colors"
          >
            <Search className="w-5 h-5 text-gray-500" />
            <span className="text-gray-600">Where would you like to go?</span>
          </button>
        ) : (
          <div className="bg-white border border-gray-200 rounded-lg p-3">
            <div className="flex items-center justify-between">
              <div className="space-y-2 flex-1">
                <div className="flex items-center gap-3 text-sm">
                  <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                  <span className="text-gray-600 truncate">
                    {userLocation?.address || "Current location"}
                  </span>
                </div>
                <div className="flex items-center gap-3">
                  <div className="w-0.5 h-4 bg-gray-300 ml-0.75"></div>
                </div>
                <div className="flex items-center gap-3 text-sm">
                  <div className="w-2 h-2 bg-red-500 rounded-full"></div>
                  <span className="font-medium text-gray-800 truncate">
                    {destination.address}
                  </span>
                </div>
              </div>
              <ArrowRight className="w-5 h-5 text-gray-400" />
            </div>
          </div>
        )}

        {/* Quick Access Buttons */}
        {!destination && (
          <div className="grid grid-cols-2 gap-2">
            <button className="bg-gray-50 hover:bg-gray-100 border border-gray-200 rounded-lg p-3 text-sm flex items-center gap-2 transition-colors">
              <Home className="w-4 h-4 text-teal-600" />
              <span className="font-medium text-gray-700">Home</span>
            </button>
            <button className="bg-gray-50 hover:bg-gray-100 border border-gray-200 rounded-lg p-3 text-sm flex items-center gap-2 transition-colors">
              <Briefcase className="w-4 h-4 text-blue-600" />
              <span className="font-medium text-gray-700">Work</span>
            </button>
          </div>
        )}
      </div>

      {/* Destination Search Modal */}
      {showDestinationSearch && (
        <DestinationSearch
          onClose={() => setShowDestinationSearch(false)}
          onSelect={handleSelectDestination}
        />
      )}
    </>
  );
};

export default SearchBar;
