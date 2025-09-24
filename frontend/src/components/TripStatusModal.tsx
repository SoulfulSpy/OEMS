import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { Star, ThumbsUp, Clock, MapPin } from 'lucide-react';
import { Trip } from '../contexts/rideStore';
import Button from './Button';

interface TripStatusModalProps {
  trip: Trip;
}

const TripStatusModal: React.FC<TripStatusModalProps> = ({ trip }) => {
  const [rating, setRating] = useState(5);
  const [tip, setTip] = useState(0);
  const [feedback, setFeedback] = useState('');
  const [showReceipt, setShowReceipt] = useState(false);

  const tipOptions = [20, 30, 50];

  const handleSubmitRating = () => {
    console.log('Rating submitted:', { rating, tip, feedback });
    setShowReceipt(true);
  };

  if (showReceipt) {
    return (
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        className="fixed inset-0 z-50 bg-black/50 flex items-end"
      >
        <motion.div
          initial={{ y: '100%' }}
          animate={{ y: 0 }}
          className="w-full bg-white rounded-t-3xl p-6 max-h-[80vh] overflow-y-auto"
        >
          <div className="text-center mb-6">
            <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
              ✅
            </div>
            <h2 className="text-2xl font-bold mb-2">Trip completed!</h2>
            <p className="text-gray-600">Thanks for riding with OEMS</p>
          </div>

          {/* Receipt */}
          <div className="glass-panel p-6 mb-6">
            <h3 className="font-semibold mb-4">Trip details</h3>
            
            <div className="space-y-4">
              <div className="flex items-start gap-3">
                <MapPin className="w-5 h-5 text-green-500 mt-1" />
                <div>
                  <p className="font-medium">From</p>
                  <p className="text-sm text-gray-600">{trip.pickupLocation.address}</p>
                </div>
              </div>
              
              <div className="flex items-start gap-3">
                <MapPin className="w-5 h-5 text-red-500 mt-1" />
                <div>
                  <p className="font-medium">To</p>
                  <p className="text-sm text-gray-600">{trip.destination.address}</p>
                </div>
              </div>
              
              <div className="flex items-center gap-3">
                <Clock className="w-5 h-5 text-gray-400" />
                <div>
                  <p className="font-medium">Duration</p>
                  <p className="text-sm text-gray-600">25 minutes</p>
                </div>
              </div>
            </div>

            <div className="border-t border-gray-200 mt-4 pt-4">
              <div className="flex justify-between items-center mb-2">
                <span>Base fare</span>
                <span>₹{trip.price - tip}</span>
              </div>
              {tip > 0 && (
                <div className="flex justify-between items-center mb-2">
                  <span>Tip</span>
                  <span>₹{tip}</span>
                </div>
              )}
              <div className="flex justify-between items-center font-semibold text-lg border-t border-gray-200 pt-2">
                <span>Total</span>
                <span>₹{trip.price + tip}</span>
              </div>
            </div>
          </div>

          <Button
            onClick={() => window.location.reload()}
            className="w-full"
          >
            Done
          </Button>
        </motion.div>
      </motion.div>
    );
  }

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="fixed inset-0 z-50 bg-black/50 flex items-end"
    >
      <motion.div
        initial={{ y: '100%' }}
        animate={{ y: 0 }}
        className="w-full bg-white rounded-t-3xl p-6"
      >
        <div className="text-center mb-6">
          <h2 className="text-2xl font-bold mb-2">Rate your trip</h2>
          <p className="text-gray-600">How was your ride with {trip.driver.name}?</p>
        </div>

        {/* Driver Info */}
        <div className="flex items-center gap-4 mb-6 p-4 bg-gray-50 rounded-xl">
          <div className="w-12 h-12 bg-gray-300 rounded-full overflow-hidden">
            <img
              src={trip.driver.profilePicture}
              alt={trip.driver.name}
              className="w-full h-full object-cover"
              onError={(e) => {
                (e.target as HTMLImageElement).src = 'https://via.placeholder.com/48x48?text=👤';
              }}
            />
          </div>
          <div>
            <h3 className="font-semibold">{trip.driver.name}</h3>
            <p className="text-sm text-gray-600">
              {trip.driver.vehicle.color} {trip.driver.vehicle.make}
            </p>
          </div>
        </div>

        {/* Rating Stars */}
        <div className="mb-6">
          <div className="flex justify-center gap-2 mb-4">
            {[1, 2, 3, 4, 5].map((star) => (
              <button
                key={star}
                onClick={() => setRating(star)}
                className="p-2"
              >
                <Star
                  className={`w-8 h-8 ${
                    star <= rating
                      ? 'fill-yellow-400 text-yellow-400'
                      : 'text-gray-300'
                  }`}
                />
              </button>
            ))}
          </div>
        </div>

        {/* Tip Options */}
        <div className="mb-6">
          <h4 className="font-semibold mb-3">Add a tip</h4>
          <div className="flex gap-3">
            {tipOptions.map((amount) => (
              <button
                key={amount}
                onClick={() => setTip(amount)}
                className={`flex-1 p-3 rounded-xl border-2 transition-all ${
                  tip === amount
                    ? 'border-teal-500 bg-teal-50'
                    : 'border-gray-200 bg-gray-50'
                }`}
              >
                ₹{amount}
              </button>
            ))}
            <button
              onClick={() => setTip(0)}
              className={`flex-1 p-3 rounded-xl border-2 transition-all ${
                tip === 0
                  ? 'border-teal-500 bg-teal-50'
                  : 'border-gray-200 bg-gray-50'
              }`}
            >
              No tip
            </button>
          </div>
        </div>

        {/* Feedback */}
        <div className="mb-6">
          <textarea
            placeholder="Leave a comment (optional)"
            value={feedback}
            onChange={(e) => setFeedback(e.target.value)}
            className="w-full p-4 border border-gray-200 rounded-xl resize-none"
            rows={3}
          />
        </div>

        {/* Submit Button */}
        <Button
          onClick={handleSubmitRating}
          className="w-full"
          size="lg"
        >
          Submit Rating
        </Button>
      </motion.div>
    </motion.div>
  );
};

export default TripStatusModal;