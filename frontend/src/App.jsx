import React, { useState } from 'react';
import Navbar from './components/Navbar';
import Hero from './components/Hero';
import CarCatalog from './components/CarCatalog';
import Footer from './components/Footer';
import AuthModal from './components/AuthModal';
import Dashboard from './components/Dashboard';

function App() {
  const [isAuthModalOpen, setAuthModalOpen] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);

  const handleLogin = (userData) => {
    setUser(userData);
    setIsAuthenticated(true);
  };

  const handleLogout = () => {
    setUser(null);
    setIsAuthenticated(false);
  };

  return (
    <>
      <Navbar user={user} onLoginClick={() => setAuthModalOpen(true)} onLogoutClick={handleLogout} />
      
      {!isAuthenticated ? (
        <>
          <Hero />
          <CarCatalog user={user} onRequireLogin={() => setAuthModalOpen(true)} />
        </>
      ) : (
        <>
          <CarCatalog user={user} onRequireLogin={() => setAuthModalOpen(true)} />
          <Dashboard user={user} onLogout={handleLogout} />
        </>
      )}
      
      <Footer />

      <AuthModal 
        isOpen={isAuthModalOpen} 
        onClose={() => setAuthModalOpen(false)} 
        onLogin={handleLogin}
      />
    </>
  );
}

export default App;
