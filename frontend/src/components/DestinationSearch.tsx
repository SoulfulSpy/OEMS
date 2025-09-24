import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { ArrowLeft, MapPin, Clock, Star } from 'lucide-react';
import { useRideStore, Location } from '../contexts/rideStore';
import Input from './Input';

interface DestinationSearchProps {
  onClose: () => void;
}

const mockLocations: Location[] = [
  {
    latitude: 22.4834,
    longitude: 88.4363,
    address: 'Salt Lake City, Kolkata, West Bengal'
  },
  {
    latitude: 22.5726,
    longitude: 88.3639,
    address: 'Park Street, Kolkata, West Bengal'
  },
  {
    latitude: 22.5448,
    longitude: 88.3426,
    address: 'Howrah Station, Howrah, West Bengal'
  }
];

const DestinationSearch: React.FC<DestinationSearchProps> = ({ onClose }) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [pickupQuery, setPickupQuery] = useState('');
  const { setDestination, setPickupLocation, userLocation, setAvailableRideOptions } = useRideStore();

  const handleSelectDestination = async (location: Location) => {
    setDestination(location);
    
    // Mock pickup location
    const pickup = userLocation || {
      latitude: 22.4734,
      longitude: 88.4263,
      address: 'Rajpur Sonarpur, West Bengal'
    };
    setPickupLocation(pickup);

    // Mock available ride options
    const mockRideOptions = [
      {
        id: 'oems-go',
        name: 'OEMS Go',
        type: 'Hatchback',
        price: 120,
        eta: 3,
        icon: '🚗',
        capacity: 4
      },
      {
        id: 'oems-sedan',
        name: 'OEMS Sedan',
        type: 'Sedan',
        price: 180,
        eta: 5,
        icon: '🚙',
        capacity: 4
      },
      {
        id: 'oems-suv',
        name: 'OEMS SUV',
        type: 'SUV',
        price: 250,
        eta: 7,
        icon: '🚐',
        capacity: 6
      }
    ];
    setAvailableRideOptions(mockRideOptions);
    
    onClose();
  };

  const filteredLocations = mockLocations.filter(location =>
    location.address.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      className="fixed inset-0 z-50 bg-white"
    >
      <div className="flex flex-col h-full">
        {/* Header */}
        <div className="flex items-center p-4 border-b border-gray-200">
          <button
            onClick={onClose}
            className="p-2 rounded-full hover:bg-gray-100 mr-2"
          >
            <ArrowLeft className="w-6 h-6" />
          </button>
          <h2 className="text-lg font-semibold">Plan your trip</h2>
        </div>

        {/* Input Fields */}
        <div className="p-4 space-y-4">
          <div className="relative">
            <div className="absolute left-4 top-6 w-2 h-2 bg-green-500 rounded-full"></div>
            <Input
              type="text"
              placeholder="Pickup location"
              value={pickupQuery}
              onChange={(e) => setPickupQuery(e.target.value)}
              className="pl-10"
            />
          </div>
          
          <div className="relative">
            <div className="absolute left-4 top-6 w-2 h-2 bg-red-500 rounded-full"></div>
            <Input
              type="text"
              placeholder="Where to?"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-10"
            />
          </div>
        </div>

        {/* Saved Places */}
        <div className="px-4 py-2">
          <h3 className="text-sm font-medium text-gray-600 mb-3">Saved places</h3>
          <div className="space-y-3">
            <button className="flex items-center gap-4 w-full text-left p-3 rounded-lg hover:bg-gray-50">
              <div className="w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center">
                🏠
              </div>
              <div>
                <div className="font-medium">Add Home</div>
                <div className="text-sm text-gray-500">Save time on future trips</div>
              </div>
            </button>
            
            <button className="flex items-center gap-4 w-full text-left p-3 rounded-lg hover:bg-gray-50">
              <div className="w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center">
                💼
              </div>
              <div>
                <div className="font-medium">Add Work</div>
                <div className="text-sm text-gray-500">Save time on future trips</div>
              </div>
            </button>
          </div>
        </div>

        {/* Search Results */}
        {searchQuery && (
          <div className="flex-1 px-4">
            <h3 className="text-sm font-medium text-gray-600 mb-3">Search results</h3>
            <div className="space-y-2">
              {filteredLocations.map((location, index) => (
                <motion.button
                  key={index}
                  onClick={() => handleSelectDestination(location)}
                  className="flex items-center gap-4 w-full text-left p-3 rounded-lg hover:bg-gray-50"
                  whileHover={{ scale: 1.02 }}
                  whileTap={{ scale: 0.98 }}
                >
                  <div className="w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center">
                    <MapPin className="w-5 h-5" />
                  </div>
                  <div className="flex-1">
                    <div className="font-medium truncate">{location.address}</div>
                  </div>
                </motion.button>
              ))}
            </div>
          </div>
        )}

        {/* Recent Searches */}
        {!searchQuery && (
          <div className="flex-1 px-4">
            <h3 className="text-sm font-medium text-gray-600 mb-3">Recent searches</h3>
            <div className="space-y-2">
              {mockLocations.slice(0, 3).map((location, index) => (
                <button
                  key={index}
                  onClick={() => handleSelectDestination(location)}
                  className="flex items-center gap-4 w-full text-left p-3 rounded-lg hover:bg-gray-50"
                >
                  <div className="w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center">
                    <Clock className="w-5 h-5" />
                  </div>
                  <div className="flex-1">
                    <div className="font-medium truncate">{location.address}</div>
                    <div className="text-sm text-gray-500">Recent</div>
                  </div>
                </button>
              ))}
            </div>
          </div>
        )}
      </div>
    </motion.div>
  );
};

export default DestinationSearch;