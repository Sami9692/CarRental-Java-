import React, { useState, useEffect } from 'react';
import { X } from 'lucide-react';
import { addCar, updateCar } from '../api';

const AdminCarModal = ({ isOpen, onClose, car, onCarSaved }) => {
  const [formData, setFormData] = useState({
    make: '',
    model: '',
    year: new Date().getFullYear(),
    licensePlate: '',
    dailyRate: 0,
    typeId: 1,
    locationId: 1,
    status: 'available',
    imageUrl: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (car) {
      setFormData({
        make: car.make,
        model: car.model,
        year: car.year,
        licensePlate: car.licensePlate || '',
        dailyRate: car.dailyRate,
        typeId: car.typeId || 1,
        locationId: car.locationId || 1,
        status: car.status,
        imageUrl: car.image || ''
      });
    } else {
      setFormData({
        make: '',
        model: '',
        year: new Date().getFullYear(),
        licensePlate: '',
        dailyRate: 50,
        typeId: 1,
        locationId: 1,
        status: 'available',
        imageUrl: ''
      });
    }
  }, [car, isOpen]);

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'year' || name === 'dailyRate' || name === 'typeId' || name === 'locationId' ? Number(value) : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      if (car) {
        await updateCar(car.id, formData);
      } else {
        await addCar(formData);
      }
      setLoading(false);
      onCarSaved();
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
      <div className="glass-panel animate-fade-in" style={{ width: '100%', maxWidth: '500px', padding: '32px', position: 'relative', maxHeight: '90vh', overflowY: 'auto' }}>
        <button onClick={onClose} style={{ position: 'absolute', top: '24px', right: '24px', background: 'transparent', color: 'var(--text-secondary)' }}>
          <X size={24} />
        </button>

        <h2 style={{ fontSize: '1.5rem', marginBottom: '24px' }}>
          {car ? 'Edit Car' : 'Add New Car'}
        </h2>

        {error && <div style={{ background: 'rgba(239, 68, 68, 0.1)', border: '1px solid #ef4444', color: '#ef4444', padding: '12px', borderRadius: '8px', marginBottom: '20px', fontSize: '0.875rem' }}>{error}</div>}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          
          <div style={{ display: 'flex', gap: '16px' }}>
            <div style={{ flex: 1 }}>
              <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Make</label>
              <input name="make" value={formData.make} onChange={handleChange} required type="text" style={{ background: 'rgba(0,0,0,0.2)', border: '1px solid rgba(255,255,255,0.05)', color: 'white', padding: '12px 16px', borderRadius: '12px', width: '100%', outline: 'none' }} />
            </div>
            <div style={{ flex: 1 }}>
              <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Model</label>
              <input name="model" value={formData.model} onChange={handleChange} required type="text" style={{ background: 'rgba(0,0,0,0.2)', border: '1px solid rgba(255,255,255,0.05)', color: 'white', padding: '12px 16px', borderRadius: '12px', width: '100%', outline: 'none' }} />
            </div>
          </div>

          <div style={{ display: 'flex', gap: '16px' }}>
            <div style={{ flex: 1 }}>
              <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Year</label>
              <input name="year" value={formData.year} onChange={handleChange} required type="number" style={{ background: 'rgba(0,0,0,0.2)', border: '1px solid rgba(255,255,255,0.05)', color: 'white', padding: '12px 16px', borderRadius: '12px', width: '100%', outline: 'none' }} />
            </div>
            <div style={{ flex: 1 }}>
              <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>License Plate</label>
              <input name="licensePlate" value={formData.licensePlate} onChange={handleChange} required type="text" style={{ background: 'rgba(0,0,0,0.2)', border: '1px solid rgba(255,255,255,0.05)', color: 'white', padding: '12px 16px', borderRadius: '12px', width: '100%', outline: 'none' }} />
            </div>
          </div>

          <div style={{ display: 'flex', gap: '16px' }}>
            <div style={{ flex: 1 }}>
              <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Daily Rate ($)</label>
              <input name="dailyRate" value={formData.dailyRate} onChange={handleChange} required type="number" step="0.01" style={{ background: 'rgba(0,0,0,0.2)', border: '1px solid rgba(255,255,255,0.05)', color: 'white', padding: '12px 16px', borderRadius: '12px', width: '100%', outline: 'none' }} />
            </div>
            <div style={{ flex: 1 }}>
              <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Status</label>
              <select name="status" value={formData.status} onChange={handleChange} style={{ background: 'rgba(0,0,0,0.2)', border: '1px solid rgba(255,255,255,0.05)', color: 'white', padding: '12px 16px', borderRadius: '12px', width: '100%', outline: 'none' }}>
                <option value="available">Available</option>
                <option value="rented">Rented</option>
                <option value="maintenance">Maintenance</option>
              </select>
            </div>
          </div>

          <div style={{ display: 'flex', gap: '16px' }}>
            <div style={{ flex: 1 }}>
              <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Type</label>
              <select name="typeId" value={formData.typeId} onChange={handleChange} style={{ background: 'rgba(0,0,0,0.2)', border: '1px solid rgba(255,255,255,0.05)', color: 'white', padding: '12px 16px', borderRadius: '12px', width: '100%', outline: 'none' }}>
                <option value={1}>Sedan</option>
                <option value={2}>SUV</option>
                <option value={3}>Hatchback</option>
                <option value={4}>Luxury</option>
              </select>
            </div>
            <div style={{ flex: 1 }}>
              <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Location</label>
              <select name="locationId" value={formData.locationId} onChange={handleChange} style={{ background: 'rgba(0,0,0,0.2)', border: '1px solid rgba(255,255,255,0.05)', color: 'white', padding: '12px 16px', borderRadius: '12px', width: '100%', outline: 'none' }}>
                <option value={1}>New York</option>
                <option value={2}>Los Angeles</option>
                <option value={3}>Chennai</option>
              </select>
            </div>
          </div>

          <div>
            <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Image URL</label>
            <input name="imageUrl" value={formData.imageUrl} onChange={handleChange} type="url" placeholder="https://..." style={{ background: 'rgba(0,0,0,0.2)', border: '1px solid rgba(255,255,255,0.05)', color: 'white', padding: '12px 16px', borderRadius: '12px', width: '100%', outline: 'none' }} />
          </div>

          <button type="submit" disabled={loading} className="btn-primary" style={{ width: '100%', padding: '14px', marginTop: '8px' }}>
            {loading ? 'Saving...' : 'Save Car'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default AdminCarModal;
