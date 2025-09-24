import { create } from 'zustand';

export interface Location {
  latitude: number;
  longitude: number;
  address: string;
}

export interface Driver {
  id: string;
  name: string;
  rating: number;
  profilePicture: string;
  vehicle: {
    make: string;
    model: string;
    color: string;
    licensePlate: string;
  };
  location: Location;
  eta: number;
}

export interface RideOption {
  id: string;
  name: string;
  type: string;
  price: number;
  eta: number;
  icon: string;
  capacity: number;
}

export interface Trip {
  id: string;
  pickupLocation: Location;
  destination: Location;
  driver: Driver;
  rideOption: RideOption;
  status: 'searching' | 'driver_assigned' | 'pickup' | 'in_progress' | 'completed' | 'cancelled';
  price: number;
  startTime: Date;
  endTime?: Date;
}

interface RideState {
  currentTrip: Trip | null;
  pickupLocation: Location | null;
  destination: Location | null;
  selectedRideOption: RideOption | null;
  availableRideOptions: RideOption[];
  userLocation: Location | null;
  isBookingRide: boolean;
  setPickupLocation: (location: Location) => void;
  setDestination: (location: Location) => void;
  setSelectedRideOption: (option: RideOption) => void;
  setAvailableRideOptions: (options: RideOption[]) => void;
  setUserLocation: (location: Location) => void;
  setCurrentTrip: (trip: Trip | null) => void;
  setBookingRide: (booking: boolean) => void;
  clearRideData: () => void;
}

export const useRideStore = create<RideState>((set) => ({
  currentTrip: null,
  pickupLocation: null,
  destination: null,
  selectedRideOption: null,
  availableRideOptions: [],
  userLocation: null,
  isBookingRide: false,
  setPickupLocation: (location) => set({ pickupLocation: location }),
  setDestination: (location) => set({ destination: location }),
  setSelectedRideOption: (option) => set({ selectedRideOption: option }),
  setAvailableRideOptions: (options) => set({ availableRideOptions: options }),
  setUserLocation: (location) => set({ userLocation: location }),
  setCurrentTrip: (trip) => set({ currentTrip: trip }),
  setBookingRide: (booking) => set({ isBookingRide: booking }),
  clearRideData: () => set({
    currentTrip: null,
    pickupLocation: null,
    destination: null,
    selectedRideOption: null,
    availableRideOptions: [],
    isBookingRide: false,
  }),
}));