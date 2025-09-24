import React from 'react';
import { motion } from 'framer-motion';
import { X, Phone, AlertTriangle, Share2, Shield } from 'lucide-react';
import Button from './Button';

interface SafetyModalProps {
  onClose: () => void;
}

const SafetyModal: React.FC<SafetyModalProps> = ({ onClose }) => {
  const handleEmergencyCall = () => {
    // Mock emergency call
    console.log('Calling emergency services...');
    window.location.href = 'tel:911';
  };

  const handleShareTrip = () => {
    // Mock share trip
    console.log('Sharing trip status...');
    if (navigator.share) {
      navigator.share({
        title: 'OEMS Trip Status',
        text: 'Track my trip with OEMS',
        url: window.location.href,
      });
    }
  };

  const handleContactSupport = () => {
    // Mock contact support
    console.log('Contacting OEMS support...');
    window.location.href = 'tel:+1234567890';
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      className="fixed inset-0 z-50 bg-black/50 flex items-center justify-center p-4"
      onClick={onClose}
    >
      <motion.div
        initial={{ scale: 0.9, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        exit={{ scale: 0.9, opacity: 0 }}
        className="glass-panel w-full max-w-md p-6"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-red-100 rounded-full flex items-center justify-center">
              <Shield className="w-5 h-5 text-red-600" />
            </div>
            <h2 className="text-xl font-bold">Safety Center</h2>
          </div>
          <button
            onClick={onClose}
            className="p-2 rounded-full hover:bg-gray-100"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="space-y-4">
          {/* Emergency Call */}
          <Button
            onClick={handleEmergencyCall}
            className="w-full bg-red-600 hover:bg-red-700 text-white"
            size="lg"
          >
            <Phone className="w-5 h-5" />
            Call Emergency (911)
          </Button>

          {/* Share Trip */}
          <Button
            onClick={handleShareTrip}
            variant="glass"
            className="w-full"
            size="lg"
          >
            <Share2 className="w-5 h-5" />
            Share Trip Status
          </Button>

          {/* Contact Support */}
          <Button
            onClick={handleContactSupport}
            variant="glass"
            className="w-full"
            size="lg"
          >
            <AlertTriangle className="w-5 h-5" />
            Contact OEMS Support
          </Button>
        </div>

        <div className="mt-6 p-4 bg-blue-50 rounded-xl">
          <h3 className="font-semibold text-blue-800 mb-2">Safety Features</h3>
          <ul className="text-sm text-blue-700 space-y-1">
            <li>• GPS tracking throughout your trip</li>
            <li>• Driver background checks</li>
            <li>• 24/7 emergency support</li>
            <li>• Real-time trip sharing</li>
          </ul>
        </div>
      </motion.div>
    </motion.div>
  );
};

export default SafetyModal;