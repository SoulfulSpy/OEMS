import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { Phone, MessageCircle } from 'lucide-react';
import { useAuthStore } from '../contexts/authStore';
import Button from '../components/Button';
import Input from '../components/Input';
import SocialLoginButtons from '../components/SocialLoginButtons';
import { sendOTP, verifyOTP } from '../services/authService';

const LoginPage: React.FC = () => {
  const [step, setStep] = useState<'phone' | 'otp' | 'profile'>('phone');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [otp, setOtp] = useState('');
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const { login, setLoading } = useAuthStore();

  const handleSendOTP = async () => {
    if (!phoneNumber || phoneNumber.length < 10) return;
    
    setIsLoading(true);
    try {
      await sendOTP(phoneNumber);
      setStep('otp');
    } catch (error) {
      console.error('Failed to send OTP:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleVerifyOTP = async () => {
    if (!otp || otp.length !== 6) return;
    
    setIsLoading(true);
    try {
      const result = await verifyOTP(phoneNumber, otp);
      if (result.isNewUser) {
        setStep('profile');
      } else {
        login(result.user);
      }
    } catch (error) {
      console.error('Failed to verify OTP:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCompleteProfile = async () => {
    if (!name || !email) return;
    
    setIsLoading(true);
    try {
      const user = {
        id: `user_${Date.now()}`,
        name,
        email,
        phone: phoneNumber,
      };
      login(user);
    } catch (error) {
      console.error('Failed to complete profile:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSocialLogin = async (provider: 'google' | 'apple') => {
    setLoading(true);
    // Simulate social login
    setTimeout(() => {
      const user = {
        id: `user_${provider}_${Date.now()}`,
        name: 'John Doe',
        email: `john.doe@${provider}.com`,
        phone: '+1234567890',
      };
      login(user);
      setLoading(false);
    }, 2000);
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center px-4 bg-gradient-to-br from-slate-50 to-slate-100">
      {/* Logo */}
      <motion.div
        initial={{ opacity: 0, y: -30 }}
        animate={{ opacity: 1, y: 0 }}
        className="mb-12"
      >
        <div className="text-6xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-teal-600 to-teal-800 mb-2">
          OEMS
        </div>
        <p className="text-center text-gray-600 text-lg font-medium">
          On-demand Electronic Mobility Service
        </p>
      </motion.div>

      {/* Main Form */}
      <motion.div
        className="glass-panel w-full max-w-md p-8 fade-in"
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ delay: 0.2 }}
      >
        {step === 'phone' && (
          <>
            <h2 className="text-2xl font-bold mb-6 text-center">Welcome to OEMS</h2>
            <p className="text-gray-600 mb-8 text-center">
              Enter your phone number to get started
            </p>
            
            <div className="space-y-6">
              <Input
                type="tel"
                placeholder="+1 (555) 123-4567"
                value={phoneNumber}
                onChange={(e) => setPhoneNumber(e.target.value)}
                icon={<Phone size={20} />}
              />
              
              <Button
                onClick={handleSendOTP}
                disabled={!phoneNumber || phoneNumber.length < 10 || isLoading}
                loading={isLoading}
                className="w-full"
              >
                Send OTP
              </Button>
            </div>
            
            <div className="mt-8">
              <div className="relative">
                <div className="absolute inset-0 flex items-center">
                  <div className="w-full border-t border-gray-200"></div>
                </div>
                <div className="relative flex justify-center text-sm">
                  <span className="bg-white px-4 text-gray-500">Or continue with</span>
                </div>
              </div>
              
              <SocialLoginButtons onLogin={handleSocialLogin} />
            </div>
          </>
        )}

        {step === 'otp' && (
          <>
            <div className="text-center mb-8">
              <MessageCircle className="w-16 h-16 mx-auto mb-4 text-teal-600" />
              <h2 className="text-2xl font-bold mb-2">Verify Your Number</h2>
              <p className="text-gray-600">
                Enter the 6-digit code sent to<br />
                <span className="font-semibold">{phoneNumber}</span>
              </p>
            </div>
            
            <div className="space-y-6">
              <Input
                type="text"
                placeholder="Enter 6-digit code"
                value={otp}
                onChange={(e) => setOtp(e.target.value)}
                maxLength={6}
              />
              
              <Button
                onClick={handleVerifyOTP}
                disabled={!otp || otp.length !== 6 || isLoading}
                loading={isLoading}
                className="w-full"
              >
                Verify Code
              </Button>
              
              <button
                onClick={() => setStep('phone')}
                className="w-full text-teal-600 font-medium"
              >
                Change Phone Number
              </button>
            </div>
          </>
        )}

        {step === 'profile' && (
          <>
            <h2 className="text-2xl font-bold mb-6 text-center">Complete Your Profile</h2>
            <p className="text-gray-600 mb-8 text-center">
              Just a few more details to get you started
            </p>
            
            <div className="space-y-6">
              <Input
                type="text"
                placeholder="Full Name"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
              
              <Input
                type="email"
                placeholder="Email Address"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              
              <Button
                onClick={handleCompleteProfile}
                disabled={!name || !email || isLoading}
                loading={isLoading}
                className="w-full"
              >
                Get Started
              </Button>
            </div>
          </>
        )}
      </motion.div>
    </div>
  );
};

export default LoginPage;