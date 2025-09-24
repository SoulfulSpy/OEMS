// Auth service calling backend API
const API_BASE = (import.meta as any).env?.VITE_API_BASE_URL || 'http://localhost:8080/api';

export const sendOTP = async (phoneNumber: string): Promise<void> => {
  const res = await fetch(`${API_BASE}/auth/send-otp`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'X-User-Phone': phoneNumber },
    body: JSON.stringify({ phone: phoneNumber })
  });
  if (!res.ok) throw new Error('Failed to send OTP');
};

export const verifyOTP = async (phoneNumber: string, otp: string): Promise<{
  isNewUser: boolean;
  user?: any;
}> => {
  const res = await fetch(`${API_BASE}/auth/verify-otp`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'X-User-Phone': phoneNumber },
    body: JSON.stringify({ phone: phoneNumber, otp })
  });
  if (!res.ok) throw new Error('Failed to verify OTP');
  return res.json();
};

export const completeProfile = async (name: string, email: string, phone: string): Promise<any> => {
  const res = await fetch(`${API_BASE}/auth/complete-profile`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'X-User-Phone': phone },
    body: JSON.stringify({ name, email, phone })
  });
  if (!res.ok) throw new Error('Failed to complete profile');
  return res.json();
};

export const googleLogin = async (idToken: string): Promise<any> => {
  const res = await fetch(`${API_BASE}/auth/google`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ idToken })
  });
  if (!res.ok) throw new Error('Google login failed');
  return res.json();
};

export const socialLogin = async (provider: 'google' | 'apple'): Promise<any> => {
  // Placeholder for Apple; Google handled via googleLogin
  return {
    user: {
      id: `user_${provider}_${Date.now()}`,
      name: 'John Doe',
      email: `john.doe@${provider}.com`,
      phone: '+1234567890',
    }
  };
};