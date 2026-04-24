import React, { useState, useEffect } from 'react';
import { X, Calendar, Shield } from 'lucide-react';
import { bookCar } from '../api';

const insuranceOptions = [
  { id: 1, name: 'Basic', description: 'Covers third-party liability only', dailyPrice: 5.00 },
  { id: 2, name: 'Standard', description: 'Third-party + collision', dailyPrice: 12.00 },
  { id: 3, name: 'Premium', description: 'Full comprehensive coverage', dailyPrice: 25.00 }
];

const BookModal = ({ isOpen, onClose, car, user, onBookingSuccess }) => {
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [insuranceTypeId, setInsuranceTypeId] = useState(1);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [estimatedTotal, setEstimatedTotal] = useState(0);

  useEffect(() => {
    if (startDate && endDate && car) {
      const start = new Date(startDate);
      const end = new Date(endDate);
      const diffTime = Math.abs(end - start);
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      
      if (diffDays > 0) {
        const selectedInsurance = insuranceOptions.find(i => i.id === insuranceTypeId);
        const dailyTotal = car.dailyRate + (selectedInsurance ? selectedInsurance.dailyPrice : 0);
        setEstimatedTotal(dailyTotal * diffDays);
      } else {
        setEstimatedTotal(0);
      }
    } else {
      setEstimatedTotal(0);
    }
  }, [startDate, endDate, insuranceTypeId, car]);

  if (!isOpen || !car || !user) return null;

  const handleBook = async (e) => {
    e.preventDefault();
    setError('');
    
    const start = new Date(startDate);
    const end = new Date(endDate);
    if (start >= end) {
      setError('Drop-off date must be after pick-up date.');
      return;
    }

    setLoading(true);
    try {
      const reservation = await bookCar(user.userId, car.id, startDate, endDate, insuranceTypeId);
      // Attach calculated amount for the PaymentModal
      reservation.amount = estimatedTotal; 
      
      setLoading(false);
      onBookingSuccess(reservation);
      onClose();
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  };

  return (
    <div style={{
      position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
      background: 'rgba(0,0,0,0.6)', backdropFilter: 'blur(8px)', zIndex: 1000,
      display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '24px'
    }}>
      <div className="glass-panel animate-fade-in" style={{ width: '100%', maxWidth: '540px', padding: '40px', position: 'relative', maxHeight: '90vh', overflowY: 'auto' }}>
        <button onClick={onClose} style={{ position: 'absolute', top: '24px', right: '24px', background: 'transparent', color: 'var(--text-secondary)' }}>
          <X size={24} />
        </button>

        <div style={{ textAlign: 'center', marginBottom: '32px' }}>
          <h2 style={{ fontSize: '1.5rem', marginBottom: '8px' }}>
            Book {car.make} {car.model}
          </h2>
          <p style={{ color: 'var(--text-secondary)' }}>Select your dates and coverage to confirm reservation.</p>
        </div>

        {error && <div style={{ background: 'rgba(239, 68, 68, 0.1)', border: '1px solid #ef4444', color: '#ef4444', padding: '12px', borderRadius: '8px', marginBottom: '20px', fontSize: '0.875rem' }}>{error}</div>}

        <form onSubmit={handleBook} style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          <div style={{ display: 'flex', gap: '16px' }}>
            <div style={{ flex: 1 }}>
              <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Pick-up Date</label>
              <div style={{ display: 'flex', alignItems: 'center', background: 'rgba(0,0,0,0.2)', padding: '12px 16px', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.05)' }}>
                <Calendar size={18} color="var(--accent-blue)" style={{ marginRight: '12px' }} />
                <input value={startDate} onChange={e=>setStartDate(e.target.value)} required type="date" style={{ background: 'transparent', border: 'none', color: 'white', width: '100%', outline: 'none' }} />
              </div>
            </div>
            <div style={{ flex: 1 }}>
              <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Drop-off Date</label>
              <div style={{ display: 'flex', alignItems: 'center', background: 'rgba(0,0,0,0.2)', padding: '12px 16px', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.05)' }}>
                <Calendar size={18} color="var(--accent-purple)" style={{ marginRight: '12px' }} />
                <input value={endDate} onChange={e=>setEndDate(e.target.value)} required type="date" style={{ background: 'transparent', border: 'none', color: 'white', width: '100%', outline: 'none' }} />
              </div>
            </div>
          </div>

          <div>
            <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Insurance Coverage</label>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {insuranceOptions.map(option => (
                <div 
                  key={option.id}
                  onClick={() => setInsuranceTypeId(option.id)}
                  style={{
                    padding: '16px', borderRadius: '12px', cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'space-between',
                    border: insuranceTypeId === option.id ? '2px solid var(--accent-blue)' : '1px solid rgba(255,255,255,0.1)',
                    background: insuranceTypeId === option.id ? 'rgba(59, 130, 246, 0.1)' : 'rgba(0,0,0,0.2)'
                  }}
                >
                  <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                    <Shield size={20} color={insuranceTypeId === option.id ? 'var(--accent-blue)' : 'var(--text-secondary)'} />
                    <div>
                      <div style={{ fontWeight: 600 }}>{option.name}</div>
                      <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>{option.description}</div>
                    </div>
                  </div>
                  <div style={{ fontWeight: 600, color: 'var(--accent-blue)' }}>
                    +${option.dailyPrice.toFixed(2)}<span style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', fontWeight: 400 }}>/day</span>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {estimatedTotal > 0 && (
            <div style={{ padding: '16px', background: 'rgba(0,0,0,0.3)', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.1)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <span style={{ color: 'var(--text-secondary)' }}>Estimated Total:</span>
              <span style={{ fontSize: '1.25rem', fontWeight: 700, color: '#4ade80' }}>${estimatedTotal.toFixed(2)}</span>
            </div>
          )}

          <button type="submit" disabled={loading} className="btn-primary" style={{ width: '100%', padding: '14px' }}>
            {loading ? 'Confirming...' : 'Continue to Payment'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default BookModal;
