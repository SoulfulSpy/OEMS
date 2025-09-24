import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { Clock, Users, CreditCard, Tag } from 'lucide-react';
import { useRideStore, RideOption } from '../contexts/rideStore';
import Button from './Button';
import { bookRide } from '../services/rideService';

const RideOptionsPanel: React.FC = () => {
  const [selectedPayment, setSelectedPayment] = useState('card-1234');
  const [promoCode, setPromoCode] = useState('');
  const [isBooking, setIsBooking] = useState(false);
  
  const {
    availableRideOptions,
    selectedRideOption,
    setSelectedRideOption,
    pickupLocation,
    destination,
    setCurrentTrip,
    setBookingRide
  } = useRideStore();

  const handleRideSelect = (option: RideOption) => {
    setSelectedRideOption(option);
  };

  const handleBookRide = async () => {
    if (!selectedRideOption || !pickupLocation || !destination) return;
    
    setIsBooking(true);
    setBookingRide(true);
    
    try {
      const trip = await bookRide({
        pickupLocation,
        destination,
        rideOption: selectedRideOption,
        paymentMethod: selectedPayment,
        promoCode
      });
      
      setCurrentTrip(trip);
    } catch (error) {
      console.error('Failed to book ride:', error);
    } finally {
      setIsBooking(false);
      setBookingRide(false);
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
        {/* Ride Options */}
        <div className="mb-6">
          <h3 className="text-lg font-semibold mb-4">Choose a ride</h3>
          <div className="space-y-3">
            {availableRideOptions.map((option) => (
              <motion.button
                key={option.id}
                onClick={() => handleRideSelect(option)}
                className={`w-full p-4 rounded-2xl border-2 transition-all ${
                  selectedRideOption?.id === option.id
                    ? 'border-teal-500 bg-teal-50'
                    : 'border-gray-200 bg-white/50'
                }`}
                whileHover={{ scale: 1.02 }}
                whileTap={{ scale: 0.98 }}
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-4">
                    <div className="text-2xl">{option.icon}</div>
                    <div className="text-left">
                      <div className="font-semibold">{option.name}</div>
                      <div className="text-sm text-gray-600 flex items-center gap-2">
                        <Clock className="w-4 h-4" />
                        {option.eta} min away
                        <Users className="w-4 h-4 ml-2" />
                        {option.capacity}
                      </div>
                    </div>
                  </div>
                  <div className="text-right">
                    <div className="text-xl font-bold">₹{option.price}</div>
                  </div>
                </div>
              </motion.button>
            ))}
          </div>
        </div>

        {/* Payment Method */}
        <div className="mb-6">
          <h4 className="font-semibold mb-3">Payment</h4>
          <button className="flex items-center gap-3 w-full p-3 rounded-xl bg-white/50 border border-gray-200">
            <CreditCard className="w-5 h-5" />
            <span>•••• •••• •••• 1234</span>
            <span className="ml-auto text-sm text-gray-500">Change</span>
          </button>
        </div>

        {/* Promo Code */}
        <div className="mb-6">
          <div className="flex items-center gap-3 p-3 rounded-xl bg-white/50 border border-gray-200">
            <Tag className="w-5 h-5" />
            <input
              type="text"
              placeholder="Add promo code"
              value={promoCode}
              onChange={(e) => setPromoCode(e.target.value)}
              className="flex-1 bg-transparent outline-none"
            />
          </div>
        </div>

        {/* Confirm Button */}
        <Button
          onClick={handleBookRide}
          disabled={!selectedRideOption || isBooking}
          loading={isBooking}
          className="w-full"
          size="lg"
        >
          {isBooking ? 'Booking your ride...' : `Confirm ${selectedRideOption?.name || 'Ride'}`}
        </Button>
      </div>
    </motion.div>
  );
};

export default RideOptionsPanel;