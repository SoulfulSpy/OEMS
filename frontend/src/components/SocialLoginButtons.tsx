import React, { useEffect, useRef } from 'react';
import Button from './Button';
import { googleLogin } from '../services/authService';
import { useAuthStore } from '../contexts/authStore';

interface SocialLoginButtonsProps {
  onLogin?: (provider: 'google' | 'apple') => void;
}

declare global {
  interface Window {
    google?: any;
  }
}

const SocialLoginButtons: React.FC<SocialLoginButtonsProps> = ({ onLogin }) => {
  const { login } = useAuthStore();
  const googleDivRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    const clientId = (import.meta as any).env?.VITE_GOOGLE_CLIENT_ID || '505510859205-kvlv43kkpojuv2astgvj4fc15dlhj7kh.apps.googleusercontent.com';
    if (!window.google || !clientId || !googleDivRef.current) return;

    window.google.accounts.id.initialize({
      client_id: clientId,
      callback: async (response: any) => {
        try {
          const { user } = await googleLogin(response.credential);
          login(user);
          (window as any).currentUser = user;
        } catch (e) {
          console.error('Google login failed', e);
        }
      },
    });

    window.google.accounts.id.renderButton(googleDivRef.current, {
      theme: 'outline',
      size: 'large',
      width: 320,
      text: 'continue_with',
      shape: 'pill',
    });
  }, [login]);

  return (
    <div className="mt-6 space-y-3">
      <div ref={googleDivRef} className="flex justify-center" />
      <Button
        variant="glass"
        className="w-full"
        onClick={() => onLogin?.('apple')}
      >
        <div className="w-5 h-5 mr-2">
          <svg viewBox="0 0 24 24" fill="currentColor">
            <path d="M17.05 20.28c-.98.95-2.05.8-3.08.35-1.09-.46-2.09-.48-3.24 0-1.44.62-2.2.44-3.06-.35C2.79 15.25 3.51 7.59 9.05 7.31c1.35.07 2.29.74 3.08.8 1.18-.24 2.31-.93 3.57-.84 1.51.12 2.65.72 3.4 1.8-3.12 1.87-2.38 5.98.48 7.13-.57 1.5-1.31 2.99-2.54 4.09l.01-.01zM12.03 7.25c-.15-2.23 1.66-4.07 3.74-4.25.29 2.58-2.34 4.5-3.74 4.25z"/>
          </svg>
        </div>
        Continue with Apple
      </Button>
    </div>
  );
};

export default SocialLoginButtons;