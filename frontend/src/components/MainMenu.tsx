import React from 'react';
import { motion } from 'framer-motion';
import { X, User, History, CreditCard, Settings, HelpCircle, LogOut } from 'lucide-react';
import { useAuthStore } from '../contexts/authStore';
import { useNavigate } from 'react-router-dom';

interface MainMenuProps {
  onClose: () => void;
}

const MainMenu: React.FC<MainMenuProps> = ({ onClose }) => {
  const { user, logout } = useAuthStore();
  const navigate = useNavigate();

  const handleNavigation = (path: string) => {
    navigate(path);
    onClose();
  };

  const handleLogout = () => {
    logout();
    onClose();
  };

  const menuItems = [
    {
      icon: <User className="w-6 h-6" />,
      label: 'Profile',
      action: () => handleNavigation('/profile'),
    },
    {
      icon: <History className="w-6 h-6" />,
      label: 'Your trips',
      action: () => handleNavigation('/history'),
    },
    {
      icon: <CreditCard className="w-6 h-6" />,
      label: 'Payment',
      action: () => handleNavigation('/payments'),
    },
    {
      icon: <Settings className="w-6 h-6" />,
      label: 'Settings',
      action: () => console.log('Settings'),
    },
    {
      icon: <HelpCircle className="w-6 h-6" />,
      label: 'Help',
      action: () => console.log('Help'),
    },
  ];

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      className="fixed inset-0 z-50 bg-black/50"
      onClick={onClose}
    >
      <motion.div
        initial={{ x: '-100%' }}
        animate={{ x: 0 }}
        exit={{ x: '-100%' }}
        transition={{ type: 'spring', damping: 30 }}
        className="w-80 max-w-[80vw] h-full bg-white shadow-2xl"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex flex-col h-full">
          {/* Header */}
          <div className="p-6 border-b border-gray-200">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-2xl font-bold text-teal-600">OEMS</h2>
              <button
                onClick={onClose}
                className="p-2 rounded-full hover:bg-gray-100"
              >
                <X className="w-6 h-6" />
              </button>
            </div>
            
            {/* User Info */}
            <div className="flex items-center gap-3">
              <div className="w-12 h-12 bg-teal-100 rounded-full flex items-center justify-center">
                <User className="w-6 h-6 text-teal-600" />
              </div>
              <div>
                <h3 className="font-semibold">{user?.name || 'User'}</h3>
                <p className="text-sm text-gray-500">{user?.phone}</p>
              </div>
            </div>
          </div>

          {/* Menu Items */}
          <div className="flex-1 py-4">
            {menuItems.map((item, index) => (
              <motion.button
                key={index}
                onClick={item.action}
                className="w-full flex items-center gap-4 px-6 py-4 hover:bg-gray-50 transition-colors"
                whileHover={{ x: 8 }}
              >
                {item.icon}
                <span className="text-lg">{item.label}</span>
              </motion.button>
            ))}
          </div>

          {/* Logout */}
          <div className="p-6 border-t border-gray-200">
            <button
              onClick={handleLogout}
              className="w-full flex items-center gap-4 text-red-600 hover:bg-red-50 px-4 py-3 rounded-lg transition-colors"
            >
              <LogOut className="w-6 h-6" />
              <span className="text-lg">Sign out</span>
            </button>
          </div>
        </div>
      </motion.div>
    </motion.div>
  );
};

export default MainMenu;