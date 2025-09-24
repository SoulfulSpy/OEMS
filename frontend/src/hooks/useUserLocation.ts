import { useCallback } from 'react';
import { useRideStore } from '../contexts/rideStore';

export const useUserLocation = () => {
  const { setUserLocation } = useRideStore();

  const getCurrentLocation = useCallback(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const location = {
            latitude: position.coords.latitude,
            longitude: position.coords.longitude,
            address: 'Current Location'
          };
          setUserLocation(location);
        },
        (error) => {
          console.error('Error getting location:', error);
          // Fallback to default location (Rajpur Sonarpur, West Bengal)
          const defaultLocation = {
            latitude: 22.4734,
            longitude: 88.4263,
            address: 'Rajpur Sonarpur, West Bengal, India'
          };
          setUserLocation(defaultLocation);
        },
        {
          enableHighAccuracy: true,
          timeout: 5000,
          maximumAge: 0
        }
      );
    } else {
      // Fallback to default location
      const defaultLocation = {
        latitude: 22.4734,
        longitude: 88.4263,
        address: 'Rajpur Sonarpur, West Bengal, India'
      };
      setUserLocation(defaultLocation);
    }
  }, [setUserLocation]);

  return { getCurrentLocation };
};