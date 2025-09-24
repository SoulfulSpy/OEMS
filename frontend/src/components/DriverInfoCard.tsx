import React from 'react';
import { motion } from 'framer-motion';
import { Phone, MessageCircle, Star, Navigation } from 'lucide-react';
import { Driver, Trip } from '../contexts/rideStore';
import Button from './Button';

interface DriverInfoCardProps {
  driver: Driver;
  trip: Trip;
}

const DriverInfoCard: React.FC<DriverInfoCardProps> = ({ driver, trip }) => {
  const handleCall = () => {
    // Mock phone call
    console.log(`Calling ${driver.name}...`);
  };

  const handleMessage = () => {
    // Mock messaging
    console.log(`Opening chat with ${driver.name}...`);
  };

  const handleCancel = () => {
    // Mock cancel ride
    console.log('Cancelling ride...');
  };

  const getStatusMessage = () => {
    switch (trip.status) {
      case 'driver_assigned':
        return `${driver.name} is on the way`;
      case 'pickup':
        return `${driver.name} has arrived`;
      case 'in_progress':
        return 'Trip in progress';
      default:
        return 'Looking for driver...';
    }
  };

  return (
    <motion.div
      initial={{ y: '100%' }}
      animate={{ y: 0 }}
      exit={{ y: '100%' }}
      transition={{ type: 'spring', damping: 30 }}
      className="absolute bottom-0 left-0 right-0 z-30"
    >
      <div className="glass-panel m-4 mb-8 p-6 rounded-t-3xl">
        {/* Status */}
        <div className="text-center mb-6">
          <h3 className="text-lg font-semibold mb-2">{getStatusMessage()}</h3>
          {trip.status === 'driver_assigned' && (
            <p className="text-gray-600">
              Arriving in {driver.eta} minutes
            </p>
          )}
        </div>

        {/* Driver Info */}
        <div className="flex items-center gap-4 mb-6">
          <div className="w-16 h-16 bg-gray-300 rounded-full overflow-hidden">
            <img
              src={driver.profilePicture}
              alt={driver.name}
              className="w-full h-full object-cover"
              onError={(e) => {
                (e.target as HTMLImageElement).src = 'https://via.placeholder.com/64x64?text=👤';
              }}
            />
          </div>
          
          <div className="flex-1">
            <div className="flex items-center gap-2 mb-1">
              <h4 className="font-semibold text-lg">{driver.name}</h4>
              <div className="flex items-center gap-1">
                <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                <span className="text-sm font-medium">{driver.rating}</span>
              </div>
            </div>
            
            <div className="text-gray-600">
              <p>{driver.vehicle.color} {driver.vehicle.make} {driver.vehicle.model}</p>
              <p className="font-mono font-semibold">{driver.vehicle.licensePlate}</p>
            </div>
          </div>
        </div>

        {/* Action Buttons */}
        <div className="flex gap-3 mb-4">
          <Button
            variant="glass"
            size="md"
            className="flex-1"
            onClick={handleCall}
          >
            <Phone className="w-5 h-5" />
            Call
          </Button>
          
          <Button
            variant="glass"
            size="md"
            className="flex-1"
            onClick={handleMessage}
          >
            <MessageCircle className="w-5 h-5" />
            Message
          </Button>
        </div>

        {/* Cancel Button */}
        {trip.status !== 'in_progress' && (
          <Button
            variant="secondary"
            size="md"
            className="w-full"
            onClick={handleCancel}
          >
            Cancel Trip
          </Button>
        )}

        {/* Trip Progress */}
        {trip.status === 'in_progress' && (
          <div className="mt-4 p-4 bg-green-50 rounded-xl">
            <div className="flex items-center gap-2 text-green-700">
              <Navigation className="w-5 h-5" />
              <span className="font-medium">En route to destination</span>
            </div>
          </div>
        )}
      </div>
    </motion.div>
  );
};

export default DriverInfoCard;