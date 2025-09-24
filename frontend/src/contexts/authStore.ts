import { create } from 'zustand';

interface User {
  id: string;
  name: string;
  email: string;
  phone: string;
  profilePicture?: string;
}

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (userData: User) => void;
  logout: () => void;
  updateUser: (userData: Partial<User>) => void;
  setLoading: (loading: boolean) => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  isAuthenticated: false,
  isLoading: false,
  login: (userData) => set({ user: userData, isAuthenticated: true }),
  logout: () => set({ user: null, isAuthenticated: false }),
  updateUser: (userData) => set((state) => ({
    user: state.user ? { ...state.user, ...userData } : null
  })),
  setLoading: (loading) => set({ isLoading: loading }),
}));