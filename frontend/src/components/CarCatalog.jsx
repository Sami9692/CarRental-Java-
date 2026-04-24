import React, { useState, useEffect } from 'react';
import { Zap, Shield, Check } from 'lucide-react';
import { fetchCars } from '../api';
import BookModal from './BookModal';
import PaymentModal from './PaymentModal';

const CarCard = ({ car, user, onRequireLogin, onSelectBook }) => {
  return (
    <div className="glass-panel" style={{ overflow: 'hidden', display: 'flex', flexDirection: 'column' }}>
      <div style={{ position: 'relative', height: '220px', overflow: 'hidden' }}>
        <img 
          src={car.image} 
          alt={`${car.make} ${car.model}`} 
          style={{ width: '100%', height: '100%', objectFit: 'cover', transition: 'transform 0.5s ease' }}
          onMouseOver={e => e.currentTarget.style.transform = 'scale(1.05)'}
          onMouseOut={e => e.currentTarget.style.transform = 'scale(1)'}
        />
        <div style={{ position: 'absolute', top: '16px', right: '16px', background: 'rgba(0,0,0,0.6)', backdropFilter: 'blur(4px)', padding: '6px 12px', borderRadius: '20px', fontSize: '0.75rem', fontWeight: 600, color: car.status.toUpperCase() === 'AVAILABLE' ? '#4ade80' : '#f87171' }}>
          {car.status.toUpperCase()}
        </div>
        <div style={{ position: 'absolute', bottom: '16px', left: '16px', background: 'rgba(0,0,0,0.6)', backdropFilter: 'blur(4px)', padding: '6px 12px', borderRadius: '8px', fontSize: '0.875rem', fontWeight: 500, display: 'flex', alignItems: 'center', gap: '6px' }}>
          <Zap size={14} color="var(--accent-blue)" /> {car.type}
        </div>
      </div>
      
      <div style={{ padding: '24px', flex: 1, display: 'flex', flexDirection: 'column' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '16px' }}>
          <div>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem', marginBottom: '4px' }}>{car.make} {car.year}</p>
            <h3 style={{ fontSize: '1.5rem', margin: 0 }}>{car.model}</h3>
          </div>
          <div style={{ textAlign: 'right' }}>
            <p style={{ color: 'var(--accent-blue)', fontSize: '1.5rem', fontWeight: 700, margin: 0 }}>${car.dailyRate}</p>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.75rem' }}>/ day</p>
          </div>
        </div>

        <div style={{ borderTop: '1px solid var(--border-color)', borderBottom: '1px solid var(--border-color)', padding: '16px 0', margin: '16px 0', flex: 1 }}>
          <ul style={{ listStyle: 'none', padding: 0, margin: 0, display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
            {car.features.map((feature, idx) => (
              <li key={idx} style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
                <Check size={14} color="var(--accent-purple)" />
                {feature}
              </li>
            ))}
          </ul>
        </div>

        <button 
          className="btn-primary" 
          onClick={() => user ? onSelectBook(car) : onRequireLogin()}
          style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '8px' }} 
          disabled={car.status.toUpperCase() !== 'AVAILABLE'}
        >
          {car.status.toUpperCase() === 'AVAILABLE' ? 'Book Now' : 'Reserved'}
        </button>
      </div>
    </div>
  );
};

const CarCatalog = ({ user, onRequireLogin }) => {
  const [cars, setCars] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedCar, setSelectedCar] = useState(null);
  const [reservationToPay, setReservationToPay] = useState(null);

  const loadCars = () => {
    setLoading(true);
    fetchCars().then(data => {
      setCars(data);
      setLoading(false);
    }).catch(err => {
      console.error(err);
      setLoading(false);
    });
  };

  useEffect(() => {
    loadCars();
  }, []);

  return (
    <section id="fleet" style={{ padding: '100px 0' }}>
      <div className="container">
        <div style={{ textAlign: 'center', marginBottom: '60px' }}>
          <h2 style={{ fontSize: '3rem', marginBottom: '16px' }}>Our Elite <span className="text-gradient">Fleet</span></h2>
          <p style={{ color: 'var(--text-secondary)', maxWidth: '600px', margin: '0 auto', fontSize: '1.125rem' }}>
            Choose from our curated selection of premium vehicles designed to deliver an unforgettable driving experience.
          </p>
        </div>

        {loading ? (
          <div style={{ textAlign: 'center', padding: '40px', color: 'var(--text-secondary)' }}>Loading cars...</div>
        ) : cars.length === 0 ? (
          <div style={{ textAlign: 'center', padding: '40px', color: 'var(--text-secondary)' }}>No cars currently available.</div>
        ) : (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: '32px' }}>
            {cars.map(car => (
              <CarCard key={car.id} car={car} user={user} onRequireLogin={onRequireLogin} onSelectBook={setSelectedCar} />
            ))}
          </div>
        )}
      </div>

      <BookModal 
        isOpen={!!selectedCar} 
        onClose={() => setSelectedCar(null)} 
        car={selectedCar} 
        user={user} 
        onBookingSuccess={(res) => {
          setReservationToPay(res);
          loadCars();
        }} 
      />

      <PaymentModal
        isOpen={!!reservationToPay}
        onClose={() => setReservationToPay(null)}
        reservation={reservationToPay}
        user={user}
        onPaymentSuccess={() => {
          alert('Payment Successful! Your booking is confirmed.');
          setReservationToPay(null);
          loadCars();
        }}
      />
    </section>
  );
};

export default CarCatalog;
