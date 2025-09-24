import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Menu, Navigation, MapPin, Car, Clock } from 'lucide-react';
import MapComponent from '../components/MapComponent';
import SearchBar from '../components/SearchBar';
import RideOptionsPanel from '../components/RideOptionsPanel';
import DriverInfoCard from '../components/DriverInfoCard';
import MainMenu from '../components/MainMenu';
import TripStatusModal from '../components/TripStatusModal';
import SafetyModal from '../components/SafetyModal';
import { useRideStore } from '../contexts/rideStore';
import { useAuthStore } from '../contexts/authStore';

const HomePage: React.FC = () => {
  const [showMenu, setShowMenu] = useState(false);
  const [showSafety, setShowSafety] = useState(false);
  const { currentTrip, destination, userLocation } = useRideStore();
  const { user } = useAuthStore();
  
  const showRideOptions = destination && !currentTrip;
  const showDriverInfo = currentTrip && ['driver_assigned', 'pickup', 'in_progress'].includes(currentTrip.status);

  return (
    <div className="min-h-screen bg-gradient-to-br from-teal-50 via-blue-50 to-indigo-50">
      {/* Header */}
      <motion.header
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        className="bg-white shadow-sm border-b border-gray-100 px-4 py-3 flex items-center justify-between"
      >
        <div className="flex items-center space-x-3">
          <button
            onClick={() => setShowMenu(true)}
            className="p-2 hover:bg-gray-100 rounded-full transition-colors"
          >
            <Menu size={24} className="text-gray-600" />
          </button>
          <div>
            <h1 className="text-xl font-bold text-teal-700">OEMS</h1>
            <p className="text-sm text-gray-500">Welcome back, {user?.name || 'User'}</p>
          </div>
        </div>
        
        {currentTrip && (
          <motion.button
            initial={{ opacity: 0, scale: 0 }}
            animate={{ opacity: 1, scale: 1 }}
            className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-full font-semibold transition-colors"
            onClick={() => setShowSafety(true)}
          >
            SOS
          </motion.button>
        )}
      </motion.header>

      {/* Main Content */}
      <div className="flex flex-col lg:flex-row h-[calc(100vh-80px)]">
        
        {/* Left Panel - Ride Booking */}
        <motion.div 
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          className="w-full lg:w-96 bg-white shadow-lg overflow-y-auto"
        >
          {/* Quick Actions */}
          <div className="p-4 border-b border-gray-100">
            <div className="grid grid-cols-3 gap-3">
              <button className="flex flex-col items-center p-3 bg-teal-50 hover:bg-teal-100 rounded-lg transition-colors">
                <Car size={24} className="text-teal-600 mb-1" />
                <span className="text-xs font-medium text-teal-700">Book Ride</span>
              </button>
              <button className="flex flex-col items-center p-3 bg-blue-50 hover:bg-blue-100 rounded-lg transition-colors">
                <Clock size={24} className="text-blue-600 mb-1" />
                <span className="text-xs font-medium text-blue-700">Schedule</span>
              </button>
              <button className="flex flex-col items-center p-3 bg-purple-50 hover:bg-purple-100 rounded-lg transition-colors">
                <Navigation size={24} className="text-purple-600 mb-1" />
                <span className="text-xs font-medium text-purple-700">Nearby</span>
              </button>
            </div>
          </div>

          {/* Location Info */}
          <div className="p-4 border-b border-gray-100">
            <div className="space-y-3">
              <div className="flex items-center space-x-3">
                <div className="w-3 h-3 bg-green-500 rounded-full"></div>
                <div className="flex-1">
                  <p className="text-sm font-medium text-gray-700">Current Location</p>
                  <p className="text-xs text-gray-500">
                    {userLocation ? `${userLocation.latitude.toFixed(4)}, ${userLocation.longitude.toFixed(4)}` : 'Getting location...'}
                  </p>
                </div>
                <MapPin size={16} className="text-gray-400" />
              </div>
              
              <div className="h-px bg-gray-200"></div>
              
              <SearchBar onMenuClick={() => setShowMenu(true)} />
            </div>
          </div>

          {/* Book Ride Button */}
          {destination && (
            <div className="p-4 border-b border-gray-100">
              <button className="w-full bg-teal-600 hover:bg-teal-700 text-white font-semibold py-3 px-4 rounded-lg transition-colors flex items-center justify-center space-x-2">
                <Car size={20} />
                <span>Book Ride</span>
              </button>
            </div>
          )}

          {/* Recent Trips */}
          <div className="p-4">
            <h3 className="text-sm font-semibold text-gray-700 mb-3">Recent Trips</h3>
            <div className="space-y-2">
              <div className="flex items-center space-x-3 p-3 bg-gray-50 hover:bg-gray-100 rounded-lg cursor-pointer transition-colors">
                <div className="w-8 h-8 bg-teal-100 rounded-full flex items-center justify-center">
                  <MapPin size={16} className="text-teal-600" />
                </div>
                <div className="flex-1">
                  <p className="text-sm font-medium text-gray-700">Downtown Mall</p>
                  <p className="text-xs text-gray-500">2 days ago • $12.50</p>
                </div>
              </div>
              <div className="flex items-center space-x-3 p-3 bg-gray-50 hover:bg-gray-100 rounded-lg cursor-pointer transition-colors">
                <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                  <MapPin size={16} className="text-blue-600" />
                </div>
                <div className="flex-1">
                  <p className="text-sm font-medium text-gray-700">Airport Terminal</p>
                  <p className="text-xs text-gray-500">1 week ago • $28.75</p>
                </div>
              </div>
              <div className="flex items-center space-x-3 p-3 bg-gray-50 hover:bg-gray-100 rounded-lg cursor-pointer transition-colors">
                <div className="w-8 h-8 bg-purple-100 rounded-full flex items-center justify-center">
                  <MapPin size={16} className="text-purple-600" />
                </div>
                <div className="flex-1">
                  <p className="text-sm font-medium text-gray-700">City Center</p>
                  <p className="text-xs text-gray-500">1 week ago • $8.25</p>
                </div>
              </div>
            </div>
          </div>
        </motion.div>

        {/* Right Panel - Map */}
        <motion.div 
          initial={{ opacity: 0, x: 20 }}
          animate={{ opacity: 1, x: 0 }}
          className="flex-1 relative"
        >
          <div className="h-full rounded-tl-lg overflow-hidden shadow-lg">
            <MapComponent />
          </div>
        </motion.div>
      </div>

      {/* Ride Options Panel */}
      <AnimatePresence>
        {showRideOptions && (
          <RideOptionsPanel />
        )}
      </AnimatePresence>

      {/* Driver Info Card */}
      <AnimatePresence>
        {showDriverInfo && (
          <DriverInfoCard driver={currentTrip.driver} trip={currentTrip} />
        )}
      </AnimatePresence>

      {/* Main Menu */}
      <AnimatePresence>
        {showMenu && (
          <MainMenu onClose={() => setShowMenu(false)} />
        )}
      </AnimatePresence>

      {/* Trip Status Modal */}
      <AnimatePresence>
        {currentTrip && currentTrip.status === 'completed' && (
          <TripStatusModal trip={currentTrip} />
        )}
      </AnimatePresence>

      {/* Safety Modal */}
      <AnimatePresence>
        {showSafety && (
          <SafetyModal onClose={() => setShowSafety(false)} />
        )}
      </AnimatePresence>
    </div>
  );
};

export default HomePage;