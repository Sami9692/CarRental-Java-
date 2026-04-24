import React from 'react';
import { Car } from 'lucide-react';

const Footer = () => {
  return (
    <footer style={{ borderTop: '1px solid var(--border-color)', paddingTop: '60px', paddingBottom: '30px', background: 'rgba(0,0,0,0.3)', marginTop: '80px' }}>
      <div className="container">
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '40px', marginBottom: '60px' }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '20px' }}>
              <div style={{ background: 'linear-gradient(135deg, var(--accent-blue), var(--accent-purple))', padding: '6px', borderRadius: '8px', display: 'flex' }}>
                <Car size={20} color="white" />
              </div>
              <span style={{ fontSize: '1.25rem', fontWeight: 700, fontFamily: 'var(--font-display)' }}>AutoElite</span>
            </div>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem', lineHeight: 1.6 }}>
              Experience the pinnacle of automotive engineering with our exclusive collection of luxury and performance vehicles.
            </p>
          </div>
          
          <div>
            <h4 style={{ marginBottom: '20px', fontSize: '1.125rem' }}>Company</h4>
            <ul style={{ listStyle: 'none', padding: 0, margin: 0, display: 'flex', flexDirection: 'column', gap: '12px' }}>
              <li><a href="#" style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>About Us</a></li>
              <li><a href="#" style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Careers</a></li>
              <li><a href="#" style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Press</a></li>
              <li><a href="#" style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Contact</a></li>
            </ul>
          </div>

          <div>
            <h4 style={{ marginBottom: '20px', fontSize: '1.125rem' }}>Services</h4>
            <ul style={{ listStyle: 'none', padding: 0, margin: 0, display: 'flex', flexDirection: 'column', gap: '12px' }}>
              <li><a href="#" style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Car Rental</a></li>
              <li><a href="#" style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Chauffeur</a></li>
              <li><a href="#" style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Corporate</a></li>
              <li><a href="#" style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Events</a></li>
            </ul>
          </div>

          <div>
            <h4 style={{ marginBottom: '20px', fontSize: '1.125rem' }}>Newsletter</h4>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem', marginBottom: '16px' }}>Subscribe to get special offers and updates.</p>
            <div style={{ display: 'flex', gap: '8px' }}>
              <input type="email" placeholder="Email Address" style={{ background: 'rgba(255,255,255,0.05)', border: '1px solid var(--border-color)', padding: '10px 16px', borderRadius: '8px', color: 'white', flex: 1, outline: 'none' }} />
              <button className="btn-primary" style={{ padding: '10px 16px' }}>Join</button>
            </div>
          </div>
        </div>

        <div style={{ textAlign: 'center', color: 'var(--text-secondary)', fontSize: '0.875rem', borderTop: '1px solid var(--border-color)', paddingTop: '24px' }}>
          &copy; {new Date().getFullYear()} AutoElite Car Rental. All rights reserved.
        </div>
      </div>
    </footer>
  );
};

export default Footer;
