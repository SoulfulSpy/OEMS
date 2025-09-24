// Mock authentication service
export const sendOTP = async (phoneNumber: string): Promise<void> => {
  return new Promise((resolve) => {
    setTimeout(() => {
      console.log(`OTP sent to ${phoneNumber}`);
      resolve();
    }, 2000);
  });
};

export const verifyOTP = async (phoneNumber: string, otp: string): Promise<{
  isNewUser: boolean;
  user?: any;
}> => {
  return new Promise((resolve) => {
    setTimeout(() => {
      // Mock verification logic
      if (otp === '123456') {
        // Existing user
        resolve({
          isNewUser: false,
          user: {
            id: 'user_existing_123',
            name: 'John Doe',
            email: 'john.doe@example.com',
            phone: phoneNumber,
          }
        });
      } else {
        // New user
        resolve({ isNewUser: true });
      }
    }, 2000);
  });
};

export const socialLogin = async (provider: 'google' | 'apple'): Promise<any> => {
  return new Promise((resolve) => {
    setTimeout(() => {
      const user = {
        id: `user_${provider}_${Date.now()}`,
        name: 'John Doe',
        email: `john.doe@${provider}.com`,
        phone: '+1234567890',
      };
      resolve({ user });
    }, 2000);
  });
};