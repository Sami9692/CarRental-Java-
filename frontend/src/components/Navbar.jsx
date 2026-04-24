import React from 'react';
import { Car, User, Search } from 'lucide-react';

const Navbar = ({ user, onLoginClick, onLogoutClick }) => {
  return (
    <nav className="glass-panel" style={{
      position: 'fixed',
      top: '20px',
      left: '50%',
      transform: 'translateX(-50%)',
      width: 'calc(100% - 48px)',
      maxWidth: '1200px',
      padding: '16px 32px',
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      zIndex: 100,
      borderRadius: '24px'
    }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '12px', cursor: 'pointer' }}>
        <div style={{
          background: 'linear-gradient(135deg, var(--accent-blue), var(--accent-purple))',
          padding: '8px',
          borderRadius: '12px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center'
        }}>
          <Car size={24} color="white" />
        </div>
        <span style={{ fontSize: '1.25rem', fontWeight: 700, fontFamily: 'var(--font-display)', letterSpacing: '0.5px' }}>
          Auto<span className="text-gradient">Elite</span>
        </span>
      </div>

      <div style={{ display: 'flex', gap: '24px', alignItems: 'center' }}>
        <a href="#fleet" style={{ fontWeight: 500, color: 'var(--text-secondary)', transition: 'color 0.3s' }} onMouseOver={e => e.target.style.color='white'} onMouseOut={e => e.target.style.color='var(--text-secondary)'}>Fleet</a>
        <a href="#services" style={{ fontWeight: 500, color: 'var(--text-secondary)', transition: 'color 0.3s' }} onMouseOver={e => e.target.style.color='white'} onMouseOut={e => e.target.style.color='var(--text-secondary)'}>Services</a>
        <a href="#about" style={{ fontWeight: 500, color: 'var(--text-secondary)', transition: 'color 0.3s' }} onMouseOver={e => e.target.style.color='white'} onMouseOut={e => e.target.style.color='var(--text-secondary)'}>About</a>
      </div>

      <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
        <button className="flex-center" style={{ background: 'transparent', color: 'white', padding: '8px', borderRadius: '50%' }}>
          <Search size={20} />
        </button>
        {user ? (
          <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <span style={{ color: 'white', fontWeight: 500 }}>Hello, {user.firstName}</span>
            <button className="btn-secondary flex-center" onClick={onLogoutClick} style={{ padding: '8px 16px', borderRadius: '12px' }}>
              Logout
            </button>
          </div>
        ) : (
          <button className="btn-primary flex-center" style={{ gap: '8px', padding: '10px 20px', borderRadius: '12px' }} onClick={onLoginClick}>
            <User size={18} />
            <span>Sign In</span>
          </button>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
