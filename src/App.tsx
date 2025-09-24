import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useAuthStore } from './contexts/authStore';
import LoginPage from './pages/LoginPage';
import HomePage from './pages/HomePage';
import RideHistoryPage from './pages/RideHistoryPage';
import ProfilePage from './pages/ProfilePage';
import PaymentMethodsPage from './pages/PaymentMethodsPage';
import './App.css';

function App() {
  const { user, isAuthenticated } = useAuthStore();

  return (
    <Router>
      <div className="app">
        <Routes>
          <Route 
            path="/login" 
            element={!isAuthenticated ? <LoginPage /> : <Navigate to="/" />} 
          />
          <Route 
            path="/" 
            element={isAuthenticated ? <HomePage /> : <Navigate to="/login" />} 
          />
          <Route 
            path="/history" 
            element={isAuthenticated ? <RideHistoryPage /> : <Navigate to="/login" />} 
          />
          <Route 
            path="/profile" 
            element={isAuthenticated ? <ProfilePage /> : <Navigate to="/login" />} 
          />
          <Route 
            path="/payments" 
            element={isAuthenticated ? <PaymentMethodsPage /> : <Navigate to="/login" />} 
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;