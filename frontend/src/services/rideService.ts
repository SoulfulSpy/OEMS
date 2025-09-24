import { Location, RideOption, Trip, Driver } from '../contexts/rideStore';

interface BookRideRequest {
  pickupLocation: Location;
  destination: Location;
  rideOption: RideOption;
  paymentMethod: string;
  promoCode?: string;
}

// Mock ride service
export const bookRide = async (request: BookRideRequest): Promise<Trip> => {
  return new Promise((resolve) => {
    setTimeout(() => {
      // Mock driver data
      const mockDriver: Driver = {
        id: 'driver_123',
        name: 'Rajesh Kumar',
        rating: 4.8,
        profilePicture: 'https://via.placeholder.com/64x64?text=RK',
        vehicle: {
          make: 'Maruti',
          model: 'Swift',
          color: 'White',
          licensePlate: 'WB 01 AB 1234'
        },
        location: {
          latitude: request.pickupLocation.latitude + 0.001,
          longitude: request.pickupLocation.longitude + 0.001,
          address: 'Driver Location'
        },
        eta: Math.floor(Math.random() * 8) + 2
      };

      const trip: Trip = {
        id: `trip_${Date.now()}`,
        pickupLocation: request.pickupLocation,
        destination: request.destination,
        driver: mockDriver,
        rideOption: request.rideOption,
        status: 'searching',
        price: request.rideOption.price,
        startTime: new Date(),
      };

      // Simulate driver matching process
      resolve(trip);
      
      // Simulate status updates
      setTimeout(() => {
        // Driver assigned
        trip.status = 'driver_assigned';
      }, 3000);
      
      setTimeout(() => {
        // Driver arrived
        trip.status = 'pickup';
      }, 8000);
      
      setTimeout(() => {
        // Trip started
        trip.status = 'in_progress';
      }, 12000);
      
      setTimeout(() => {
        // Trip completed
        trip.status = 'completed';
        trip.endTime = new Date();
      }, 20000);
      
    }, 2000);
  });
};

export const cancelRide = async (tripId: string): Promise<void> => {
  return new Promise((resolve) => {
    setTimeout(() => {
      console.log(`Trip ${tripId} cancelled`);
      resolve();
    }, 1000);
  });
};

export const getRideEstimate = async (
  pickup: Location,
  destination: Location
): Promise<RideOption[]> => {
  return new Promise((resolve) => {
    setTimeout(() => {
      const mockOptions: RideOption[] = [
        {
          id: 'oems-go',
          name: 'OEMS Go',
          type: 'Hatchback',
          price: 120,
          eta: 3,
          icon: '🚗',
          capacity: 4
        },
        {
          id: 'oems-sedan',
          name: 'OEMS Sedan',
          type: 'Sedan',
          price: 180,
          eta: 5,
          icon: '🚙',
          capacity: 4
        },
        {
          id: 'oems-suv',
          name: 'OEMS SUV',
          type: 'SUV',
          price: 250,
          eta: 7,
          icon: '🚐',
          capacity: 6
        }
      ];
      resolve(mockOptions);
    }, 1500);
  });
};