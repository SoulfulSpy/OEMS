import React, { useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import { LatLngTuple } from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { useRideStore } from '../contexts/rideStore';
import { useUserLocation } from '../hooks/useUserLocation';

// Fix for default markers in react-leaflet
import L from 'leaflet';
delete (L.Icon.Default.prototype as any)._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});

const defaultCenter: LatLngTuple = [22.4734, 88.4263]; // Rajpur Sonarpur, West Bengal

const MapUpdater: React.FC<{ center: LatLngTuple }> = ({ center }) => {
  const map = useMap();
  
  useEffect(() => {
    map.setView(center, map.getZoom());
  }, [center, map]);
  
  return null;
};

const MapComponent: React.FC = () => {
  const { userLocation, pickupLocation, destination, currentTrip } = useRideStore();
  const { getCurrentLocation } = useUserLocation();
  
  const mapCenter: LatLngTuple = userLocation 
    ? [userLocation.latitude, userLocation.longitude]
    : defaultCenter;

  useEffect(() => {
    getCurrentLocation();
  }, [getCurrentLocation]);

  return (
    <div className="w-full h-full relative bg-gray-100 rounded-lg overflow-hidden">
      <MapContainer
        center={mapCenter}
        zoom={15}
        className="w-full h-full"
        zoomControl={true}
        attributionControl={false}
        style={{ height: '100%', width: '100%' }}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        />
        
        <MapUpdater center={mapCenter} />
        
        {/* User Location Marker */}
        {userLocation && (
          <Marker position={[userLocation.latitude, userLocation.longitude]}>
            <Popup>
              <div className="text-center">
                <strong className="text-teal-700">Your Location</strong>
                <br />
                <span className="text-sm text-gray-600">Current position</span>
              </div>
            </Popup>
          </Marker>
        )}
        
        {/* Pickup Location Marker */}
        {pickupLocation && (
          <Marker position={[pickupLocation.latitude, pickupLocation.longitude]}>
            <Popup>
              <div className="text-center">
                <strong className="text-blue-700">Pickup Location</strong>
                <br />
                <span className="text-sm text-gray-600">Trip starting point</span>
              </div>
            </Popup>
          </Marker>
        )}
        
        {/* Destination Marker */}
        {destination && (
          <Marker position={[destination.latitude, destination.longitude]}>
            <Popup>
              <div className="text-center">
                <strong className="text-green-700">Destination</strong>
                <br />
                <span className="text-sm text-gray-600">{destination.address}</span>
              </div>
            </Popup>
          </Marker>
        )}
        
        {/* Driver Location Marker */}
        {currentTrip?.driver && (
          <Marker position={[currentTrip.driver.location.latitude, currentTrip.driver.location.longitude]}>
            <Popup>
              <div className="text-center">
                <strong className="text-purple-700">{currentTrip.driver.name}</strong>
                <br />
                <span className="text-sm text-gray-600">
                  {currentTrip.driver.vehicle.make} {currentTrip.driver.vehicle.model}
                </span>
              </div>
            </Popup>
          </Marker>
        )}
      </MapContainer>
      
      {/* Map Controls */}
      <div className="absolute bottom-4 right-4 z-10 space-y-2">
        <button
          onClick={getCurrentLocation}
          className="w-12 h-12 bg-white hover:bg-gray-50 rounded-full shadow-lg flex items-center justify-center transition-all duration-200 hover:scale-105"
          title="Get current location"
        >
          <span className="text-xl">📍</span>
        </button>
      </div>
      
      {/* Map Legend/Info */}
      <div className="absolute top-4 left-4 z-10 bg-white rounded-lg shadow-md p-3 max-w-xs">
        <h4 className="text-sm font-semibold text-gray-700 mb-2">Map View</h4>
        <div className="space-y-1 text-xs">
          {userLocation && (
            <div className="flex items-center space-x-2">
              <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
              <span className="text-gray-600">Your location</span>
            </div>
          )}
          {destination && (
            <div className="flex items-center space-x-2">
              <div className="w-2 h-2 bg-green-500 rounded-full"></div>
              <span className="text-gray-600">Destination</span>
            </div>
          )}
          {currentTrip?.driver && (
            <div className="flex items-center space-x-2">
              <div className="w-2 h-2 bg-purple-500 rounded-full"></div>
              <span className="text-gray-600">Driver location</span>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default MapComponent;