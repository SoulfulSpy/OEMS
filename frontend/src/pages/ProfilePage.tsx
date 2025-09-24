import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { ArrowLeft, Camera, Edit3 } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../contexts/authStore';
import Button from '../components/Button';
import Input from '../components/Input';

const ProfilePage: React.FC = () => {
  const navigate = useNavigate();
  const { user, updateUser } = useAuthStore();
  const [isEditing, setIsEditing] = useState(false);
  const [name, setName] = useState(user?.name || '');
  const [email, setEmail] = useState(user?.email || '');

  const handleSave = () => {
    updateUser({ name, email });
    setIsEditing(false);
  };

  const handleCancel = () => {
    setName(user?.name || '');
    setEmail(user?.email || '');
    setIsEditing(false);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="glass-panel mx-4 mt-12 mb-6 p-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <button
              onClick={() => navigate('/')}
              className="p-2 rounded-full hover:bg-white/20"
            >
              <ArrowLeft className="w-6 h-6" />
            </button>
            <h1 className="text-lg font-semibold">Profile</h1>
          </div>
          
          <button
            onClick={() => setIsEditing(!isEditing)}
            className="p-2 rounded-full hover:bg-white/20"
          >
            <Edit3 className="w-5 h-5" />
          </button>
        </div>
      </div>

      <div className="px-4 space-y-6">
        {/* Profile Picture */}
        <motion.div
          className="glass-panel p-6 text-center"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
        >
          <div className="relative inline-block mb-4">
            <div className="w-24 h-24 bg-teal-100 rounded-full flex items-center justify-center text-3xl font-bold text-teal-600 mx-auto">
              {user?.name?.charAt(0).toUpperCase() || 'U'}
            </div>
            <button className="absolute bottom-0 right-0 w-8 h-8 bg-teal-600 text-white rounded-full flex items-center justify-center">
              <Camera className="w-4 h-4" />
            </button>
          </div>
          <h2 className="text-xl font-semibold">{user?.name || 'User'}</h2>
          <p className="text-gray-600">{user?.email}</p>
        </motion.div>

        {/* Personal Information */}
        <motion.div
          className="glass-panel p-6"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
        >
          <h3 className="text-lg font-semibold mb-4">Personal Information</h3>
          
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Full Name
              </label>
              {isEditing ? (
                <Input
                  type="text"
                  placeholder="Enter your full name"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                />
              ) : (
                <div className="pill-input opacity-60">
                  {user?.name || 'Not set'}
                </div>
              )}
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Email Address
              </label>
              {isEditing ? (
                <Input
                  type="email"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              ) : (
                <div className="pill-input opacity-60">
                  {user?.email || 'Not set'}
                </div>
              )}
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Phone Number
              </label>
              <div className="pill-input opacity-60">
                {user?.phone || 'Not set'}
              </div>
            </div>
          </div>
          
          {isEditing && (
            <div className="flex gap-3 mt-6">
              <Button
                onClick={handleSave}
                className="flex-1"
              >
                Save Changes
              </Button>
              <Button
                onClick={handleCancel}
                variant="secondary"
                className="flex-1"
              >
                Cancel
              </Button>
            </div>
          )}
        </motion.div>

        {/* Account Settings */}
        <motion.div
          className="glass-panel p-6"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
        >
          <h3 className="text-lg font-semibold mb-4">Account Settings</h3>
          
          <div className="space-y-3">
            <button className="w-full flex items-center justify-between p-3 rounded-xl hover:bg-white/20 transition-colors">
              <span>Notification Preferences</span>
              <span className="text-gray-500">›</span>
            </button>
            
            <button className="w-full flex items-center justify-between p-3 rounded-xl hover:bg-white/20 transition-colors">
              <span>Privacy Settings</span>
              <span className="text-gray-500">›</span>
            </button>
            
            <button className="w-full flex items-center justify-between p-3 rounded-xl hover:bg-white/20 transition-colors">
              <span>Language</span>
              <div className="flex items-center gap-2">
                <span className="text-gray-500">English</span>
                <span className="text-gray-500">›</span>
              </div>
            </button>
          </div>
        </motion.div>

        {/* Emergency Contacts */}
        <motion.div
          className="glass-panel p-6"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
        >
          <h3 className="text-lg font-semibold mb-4">Emergency Contacts</h3>
          <p className="text-gray-600 mb-4">Add trusted contacts who can track your trips</p>
          
          <Button
            variant="secondary"
            className="w-full"
            onClick={() => console.log('Add emergency contact')}
          >
            Add Emergency Contact
          </Button>
        </motion.div>
      </div>
    </div>
  );
};

export default ProfilePage;