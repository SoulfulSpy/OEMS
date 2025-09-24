import React, { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { useNavigate } from "react-router-dom";
import {
  Car,
  Navigation,
  Star,
  Shield,
  Clock,
  MapPin,
  Globe,
  Menu,
  Zap,
  Users,
} from "lucide-react";
import MainMenu from "../components/MainMenu";
import { useRideStore } from "../contexts/rideStore";

const HomePage: React.FC = () => {
  const navigate = useNavigate();
  const [showMenu, setShowMenu] = useState(false);
  const [pickupLocation, setPickupLocation] = useState("");
  const [destination, setDestination] = useState("");
  const {
    setDestination: setRideDestination,
    setPickupLocation: setRidePickup,
  } = useRideStore();

  const handleBookRide = () => {
    if (pickupLocation && destination) {
      setRidePickup({
        latitude: 0,
        longitude: 0,
        address: pickupLocation,
      });
      setRideDestination({
        latitude: 0,
        longitude: 0,
        address: destination,
      });
      navigate("/dashboard");
    }
  };

  const handleScheduleRide = () => {
    console.log("Schedule ride for later");
  };

  return (
    <div className="min-h-screen" style={{ backgroundColor: "#F4F4F4" }}>
      {/* Navigation Header */}
      <motion.header
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        style={{ backgroundColor: "#008080" }}
        className="text-white px-6 py-4 shadow-lg"
      >
        <div className="max-w-7xl mx-auto flex items-center justify-between">
          {/* Logo and Navigation */}
          <div className="flex items-center space-x-8">
            <div className="flex items-center space-x-2">
              <div className="w-8 h-8 bg-white/20 rounded-full flex items-center justify-center">
                <Car size={20} className="text-white" />
              </div>
              <h1 className="text-2xl font-bold">OEMS</h1>
            </div>
            <nav className="hidden md:flex space-x-8">
              <button className="hover:opacity-80 transition-opacity font-medium">
                Rides
              </button>
              <button className="hover:opacity-80 transition-opacity font-medium">
                Drive
              </button>
              <button className="hover:opacity-80 transition-opacity font-medium">
                Business
              </button>
              <button className="hover:opacity-80 transition-opacity font-medium">
                Safety
              </button>
            </nav>
          </div>

          {/* Right Navigation */}
          <div className="flex items-center space-x-4">
            <button className="flex items-center space-x-1 hover:opacity-80 transition-opacity">
              <Globe size={18} />
              <span className="hidden sm:inline">EN</span>
            </button>
            <button className="hover:opacity-80 transition-opacity">
              Support
            </button>
            <button
              onClick={() => navigate("/login")}
              className="hover:opacity-80 transition-opacity font-medium"
            >
              Sign In
            </button>
            <button
              style={{ backgroundColor: "#98FF98", color: "#36454F" }}
              className="px-6 py-2 rounded-full hover:opacity-90 transition-opacity font-semibold"
            >
              Join Now
            </button>
            <button
              onClick={() => setShowMenu(true)}
              className="md:hidden p-2 hover:bg-white/10 rounded"
            >
              <Menu size={20} />
            </button>
          </div>
        </div>
      </motion.header>

      {/* Hero Section */}
      <div className="relative overflow-hidden">
        {/* Background Pattern */}
        <div className="absolute inset-0 opacity-5">
          <div
            className="absolute top-20 left-20 w-32 h-32 rounded-full"
            style={{ backgroundColor: "#008080" }}
          ></div>
          <div
            className="absolute top-40 right-32 w-24 h-24 rounded-full"
            style={{ backgroundColor: "#98FF98" }}
          ></div>
          <div
            className="absolute bottom-32 left-1/3 w-16 h-16 rounded-full"
            style={{ backgroundColor: "#008080" }}
          ></div>
        </div>

        <div className="max-w-7xl mx-auto px-6 py-16">
          <div className="text-center mb-16">
            {/* Main Heading */}
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.2 }}
            >
              <h1
                className="text-6xl lg:text-7xl font-bold mb-6"
                style={{ color: "#36454F" }}
              >
                Premium Rides,
                <br />
                <span style={{ color: "#008080" }}>Everywhere</span>
              </h1>
              <p
                className="text-xl lg:text-2xl mb-8"
                style={{ color: "#36454F" }}
              >
                Experience the future of transportation with OEMS
              </p>
            </motion.div>

            {/* Promo Banner */}
            <motion.div
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ delay: 0.4 }}
              className="inline-flex items-center space-x-3 px-6 py-3 rounded-full mb-12"
              style={{ backgroundColor: "#98FF98" }}
            >
              <Star size={20} style={{ color: "#008080" }} />
              <span className="font-semibold" style={{ color: "#36454F" }}>
                50% OFF first 3 rides • New riders only
              </span>
            </motion.div>

            {/* Booking Form */}
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.6 }}
              className="max-w-2xl mx-auto bg-white rounded-2xl shadow-2xl p-8 mb-16"
            >
              <h3
                className="text-2xl font-bold mb-6"
                style={{ color: "#36454F" }}
              >
                Book your ride
              </h3>

              <div className="space-y-4">
                <div className="relative">
                  <div className="absolute left-4 top-1/2 transform -translate-y-1/2 flex flex-col items-center">
                    <div
                      className="w-3 h-3 rounded-full"
                      style={{ backgroundColor: "#008080" }}
                    ></div>
                    <div
                      className="w-0.5 h-12 mt-1"
                      style={{ backgroundColor: "#98FF98" }}
                    ></div>
                  </div>
                  <input
                    type="text"
                    placeholder="Pick-up location"
                    value={pickupLocation}
                    onChange={(e) => setPickupLocation(e.target.value)}
                    className="w-full pl-12 pr-4 py-4 border-2 border-gray-200 rounded-xl text-lg focus:outline-none focus:border-[#008080] transition-colors"
                    style={{ color: "#36454F" }}
                  />
                </div>

                <div className="relative">
                  <div className="absolute left-4 top-1/2 transform -translate-y-1/2">
                    <div
                      className="w-3 h-3 rounded-full border-2"
                      style={{
                        borderColor: "#008080",
                        backgroundColor: "white",
                      }}
                    ></div>
                  </div>
                  <input
                    type="text"
                    placeholder="Where to?"
                    value={destination}
                    onChange={(e) => setDestination(e.target.value)}
                    className="w-full pl-12 pr-4 py-4 border-2 border-gray-200 rounded-xl text-lg focus:outline-none focus:border-[#008080] transition-colors"
                    style={{ color: "#36454F" }}
                  />
                </div>

                <div className="flex space-x-4 mt-6">
                  <button
                    onClick={handleBookRide}
                    disabled={!pickupLocation || !destination}
                    className="flex-1 py-4 px-6 rounded-xl font-bold text-lg text-white transition-all transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none"
                    style={{ backgroundColor: "#008080" }}
                  >
                    Book Now
                  </button>
                  <button
                    onClick={handleScheduleRide}
                    className="px-6 py-4 rounded-xl font-semibold text-lg border-2 transition-all hover:scale-105"
                    style={{ borderColor: "#008080", color: "#008080" }}
                  >
                    Schedule
                  </button>
                </div>
              </div>
            </motion.div>
          </div>

          {/* Features Section */}
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.8 }}
            className="grid md:grid-cols-3 gap-8 mb-16"
          >
            <div className="text-center p-6 bg-white rounded-2xl shadow-lg hover:shadow-xl transition-shadow">
              <div
                className="w-16 h-16 mx-auto mb-4 rounded-full flex items-center justify-center"
                style={{ backgroundColor: "#008080" }}
              >
                <Zap size={32} className="text-white" />
              </div>
              <h3
                className="text-xl font-bold mb-2"
                style={{ color: "#36454F" }}
              >
                Fast & Reliable
              </h3>
              <p style={{ color: "#36454F" }}>
                Get picked up in minutes with our efficient dispatch system
              </p>
            </div>

            <div className="text-center p-6 bg-white rounded-2xl shadow-lg hover:shadow-xl transition-shadow">
              <div
                className="w-16 h-16 mx-auto mb-4 rounded-full flex items-center justify-center"
                style={{ backgroundColor: "#008080" }}
              >
                <Shield size={32} className="text-white" />
              </div>
              <h3
                className="text-xl font-bold mb-2"
                style={{ color: "#36454F" }}
              >
                Safety First
              </h3>
              <p style={{ color: "#36454F" }}>
                Background-checked drivers and real-time ride tracking
              </p>
            </div>

            <div className="text-center p-6 bg-white rounded-2xl shadow-lg hover:shadow-xl transition-shadow">
              <div
                className="w-16 h-16 mx-auto mb-4 rounded-full flex items-center justify-center"
                style={{ backgroundColor: "#008080" }}
              >
                <Users size={32} className="text-white" />
              </div>
              <h3
                className="text-xl font-bold mb-2"
                style={{ color: "#36454F" }}
              >
                Premium Experience
              </h3>
              <p style={{ color: "#36454F" }}>
                Clean vehicles, professional drivers, and excellent service
              </p>
            </div>
          </motion.div>

          {/* CTA Section */}
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 1 }}
            className="text-center bg-white rounded-2xl p-12 shadow-lg"
          >
            <h2
              className="text-4xl font-bold mb-4"
              style={{ color: "#36454F" }}
            >
              Ready to ride?
            </h2>
            <p className="text-xl mb-8" style={{ color: "#36454F" }}>
              Join thousands of satisfied riders who choose OEMS every day
            </p>
            <div className="flex justify-center space-x-4">
              <button
                onClick={() => navigate("/login")}
                className="px-8 py-4 rounded-xl font-bold text-lg text-white transition-all transform hover:scale-105"
                style={{ backgroundColor: "#008080" }}
              >
                Get Started
              </button>
              <button
                className="px-8 py-4 rounded-xl font-bold text-lg border-2 transition-all hover:scale-105"
                style={{ borderColor: "#008080", color: "#008080" }}
              >
                Learn More
              </button>
            </div>
          </motion.div>
        </div>
      </div>

      {/* Main Menu */}
      <AnimatePresence>
        {showMenu && <MainMenu onClose={() => setShowMenu(false)} />}
      </AnimatePresence>
    </div>
  );
};

export default HomePage;
