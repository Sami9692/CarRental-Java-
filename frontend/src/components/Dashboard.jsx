import React, { useState, useEffect } from 'react';
import { Calendar, CreditCard, Settings, LogOut, Loader, CheckCircle, Shield, Car, List, Edit2, Trash2, Plus } from 'lucide-react';
import { fetchUserReservations, fetchUserCards, fetchAllCars, fetchAllReservations, deleteCar } from '../api';
import PaymentModal from './PaymentModal';
import AdminCarModal from './AdminCarModal';

const Dashboard = ({ user, onLogout }) => {
  const [activeTab, setActiveTab] = useState('reservations'); // 'reservations', 'payments', 'admin-fleet', 'admin-reservations'
  const [reservations, setReservations] = useState([]);
  const [cards, setCards] = useState([]);
  const [loading, setLoading] = useState(true);
  
  // Admin state
  const [allCars, setAllCars] = useState([]);
  const [allReservations, setAllReservations] = useState([]);
  const [selectedCarToEdit, setSelectedCarToEdit] = useState(null);
  const [isAdminCarModalOpen, setIsAdminCarModalOpen] = useState(false);

  const [selectedReservationToPay, setSelectedReservationToPay] = useState(null);

  const loadData = () => {
    if (user && user.userId) {
      setLoading(true);
      const promises = [
        fetchUserReservations(user.userId),
        fetchUserCards(user.userId)
      ];

      if (user.isAdmin) {
        promises.push(fetchAllCars());
        promises.push(fetchAllReservations());
      }

      Promise.all(promises).then((results) => {
        setReservations(results[0]);
        setCards(results[1]);
        if (user.isAdmin) {
          setAllCars(results[2] || []);
          setAllReservations(results[3] || []);
        }
        setLoading(false);
      }).catch(err => {
        console.error(err);
        setLoading(false);
      });
    }
  };

  useEffect(() => {
    loadData();
  }, [user]);

  const handleDeleteCar = async (carId) => {
    if (window.confirm('Are you sure you want to delete this car?')) {
      try {
        await deleteCar(carId);
        loadData();
      } catch (err) {
        alert(err.message);
      }
    }
  };

  if (!user) return null;

  return (
    <div className="container" style={{ paddingTop: '140px', paddingBottom: '80px' }}>
      <div style={{ display: 'grid', gridTemplateColumns: '280px 1fr', gap: '32px' }}>
        
        {/* Sidebar */}
        <div className="glass-panel" style={{ padding: '32px 24px', height: 'fit-content' }}>
          <div style={{ textAlign: 'center', marginBottom: '32px' }}>
            <div style={{ width: '80px', height: '80px', borderRadius: '50%', background: 'linear-gradient(135deg, var(--accent-blue), var(--accent-purple))', margin: '0 auto 16px', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '2rem', fontWeight: 700 }}>
              {user.firstName.charAt(0)}
            </div>
            <h3 style={{ margin: '0 0 4px 0' }}>{user.firstName} {user.lastName}</h3>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem', margin: 0 }}>{user.email}</p>
            {user.isAdmin && (
              <div style={{ display: 'inline-flex', alignItems: 'center', gap: '4px', background: 'rgba(59, 130, 246, 0.2)', color: 'var(--accent-blue)', padding: '4px 8px', borderRadius: '12px', fontSize: '0.75rem', fontWeight: 600, marginTop: '8px' }}>
                <Shield size={12} /> Administrator
              </div>
            )}
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            <div style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: 'var(--text-secondary)', letterSpacing: '1px', marginBottom: '4px', paddingLeft: '12px' }}>Personal</div>
            <button 
              className="btn-secondary" 
              onClick={() => setActiveTab('reservations')}
              style={{ display: 'flex', alignItems: 'center', gap: '12px', justifyContent: 'flex-start', border: 'none', background: activeTab === 'reservations' ? 'rgba(255,255,255,0.05)' : 'transparent' }}>
              <Calendar size={18} color={activeTab === 'reservations' ? "var(--accent-blue)" : "var(--text-secondary)"} /> My Reservations
            </button>
            <button 
              className="btn-secondary" 
              onClick={() => setActiveTab('payments')}
              style={{ display: 'flex', alignItems: 'center', gap: '12px', justifyContent: 'flex-start', border: 'none', background: activeTab === 'payments' ? 'rgba(255,255,255,0.05)' : 'transparent' }}>
              <CreditCard size={18} color={activeTab === 'payments' ? "var(--accent-purple)" : "var(--text-secondary)"} /> Payment Methods
            </button>
            <button className="btn-secondary" style={{ display: 'flex', alignItems: 'center', gap: '12px', justifyContent: 'flex-start', border: 'none' }}>
              <Settings size={18} color="var(--text-secondary)" /> Settings
            </button>

            {user.isAdmin && (
              <>
                <div style={{ height: '1px', background: 'var(--border-color)', margin: '16px 0' }}></div>
                <div style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: 'var(--text-secondary)', letterSpacing: '1px', marginBottom: '4px', paddingLeft: '12px' }}>Admin Panel</div>
                <button 
                  className="btn-secondary" 
                  onClick={() => setActiveTab('admin-fleet')}
                  style={{ display: 'flex', alignItems: 'center', gap: '12px', justifyContent: 'flex-start', border: 'none', background: activeTab === 'admin-fleet' ? 'rgba(255,255,255,0.05)' : 'transparent' }}>
                  <Car size={18} color={activeTab === 'admin-fleet' ? "var(--accent-blue)" : "var(--text-secondary)"} /> Fleet Management
                </button>
                <button 
                  className="btn-secondary" 
                  onClick={() => setActiveTab('admin-reservations')}
                  style={{ display: 'flex', alignItems: 'center', gap: '12px', justifyContent: 'flex-start', border: 'none', background: activeTab === 'admin-reservations' ? 'rgba(255,255,255,0.05)' : 'transparent' }}>
                  <List size={18} color={activeTab === 'admin-reservations' ? "var(--accent-purple)" : "var(--text-secondary)"} /> Global Reservations
                </button>
              </>
            )}

            <div style={{ height: '1px', background: 'var(--border-color)', margin: '16px 0' }}></div>
            <button className="btn-secondary" onClick={onLogout} style={{ display: 'flex', alignItems: 'center', gap: '12px', justifyContent: 'flex-start', border: 'none', color: '#f87171' }}>
              <LogOut size={18} /> Logout
            </button>
          </div>
        </div>

        {/* Main Content */}
        <div className="glass-panel" style={{ padding: '40px' }}>
          {activeTab === 'reservations' ? (
            <>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px' }}>
                <h2 style={{ margin: 0 }}>Active Reservations</h2>
                <a href="#fleet" className="btn-primary flex-center" style={{ padding: '8px 16px', fontSize: '0.875rem', textDecoration: 'none' }}>Book New Car</a>
              </div>

              {loading ? (
                <div style={{ textAlign: 'center', padding: '40px', color: 'var(--text-secondary)' }}>
                  <Loader className="animate-spin" style={{ margin: '0 auto 16px' }} />
                  Loading reservations...
                </div>
              ) : reservations.length === 0 ? (
                <div style={{ background: 'rgba(0,0,0,0.2)', borderRadius: '16px', padding: '32px', textAlign: 'center', border: '1px dashed var(--border-color)' }}>
                  <Calendar size={48} color="var(--text-secondary)" style={{ margin: '0 auto 16px', opacity: 0.5 }} />
                  <h4 style={{ fontSize: '1.25rem', marginBottom: '8px' }}>No active reservations</h4>
                  <p style={{ color: 'var(--text-secondary)' }}>You don't have any cars booked at the moment. Browse our fleet to start your next journey.</p>
                </div>
              ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                  {reservations.map(res => (
                    <div key={res.reservationId} style={{ background: 'rgba(0,0,0,0.2)', padding: '24px', borderRadius: '12px', border: '1px solid var(--border-color)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                      <div>
                        <h4 style={{ margin: '0 0 8px 0', fontSize: '1.25rem' }}>Reservation #{res.reservationId} (Car ID: {res.carId})</h4>
                        <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem', margin: '0 0 12px 0' }}>
                          {res.startDate} to {res.endDate}
                        </p>
                        <div style={{ display: 'inline-block', padding: '6px 12px', background: res.status.toLowerCase() === 'pending' ? 'rgba(245, 158, 11, 0.1)' : 'rgba(59, 130, 246, 0.1)', color: res.status.toLowerCase() === 'pending' ? '#f59e0b' : 'var(--accent-blue)', borderRadius: '8px', fontSize: '0.875rem', fontWeight: 500, textTransform: 'capitalize' }}>
                          {res.status}
                        </div>
                      </div>
                      <div>
                        {res.status.toLowerCase() === 'pending' ? (
                          <button 
                            className="btn-primary" 
                            onClick={() => setSelectedReservationToPay(res)}
                            style={{ padding: '10px 20px', borderRadius: '8px' }}>
                            Pay Now
                          </button>
                        ) : (
                          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: '#4ade80', fontWeight: 500 }}>
                            <CheckCircle size={20} /> Paid
                          </div>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </>
          ) : activeTab === 'payments' ? (
            <>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px' }}>
                <h2 style={{ margin: 0 }}>Payment Methods</h2>
                <button className="btn-primary flex-center" onClick={() => setSelectedReservationToPay({ reservationId: 'NEW_CARD_ONLY' })} style={{ padding: '8px 16px', fontSize: '0.875rem' }}>+ Add Card</button>
              </div>

              {loading ? (
                <div style={{ textAlign: 'center', padding: '40px', color: 'var(--text-secondary)' }}>
                  <Loader className="animate-spin" style={{ margin: '0 auto 16px' }} />
                  Loading cards...
                </div>
              ) : cards.length === 0 ? (
                <div style={{ background: 'rgba(0,0,0,0.2)', borderRadius: '16px', padding: '32px', textAlign: 'center', border: '1px dashed var(--border-color)' }}>
                  <CreditCard size={48} color="var(--text-secondary)" style={{ margin: '0 auto 16px', opacity: 0.5 }} />
                  <h4 style={{ fontSize: '1.25rem', marginBottom: '8px' }}>No saved cards</h4>
                  <p style={{ color: 'var(--text-secondary)' }}>You haven't saved any payment methods yet.</p>
                </div>
              ) : (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '20px' }}>
                  {cards.map(c => (
                    <div key={c.cardId} style={{ background: 'linear-gradient(135deg, rgba(255,255,255,0.05), rgba(255,255,255,0.02))', padding: '24px', borderRadius: '16px', border: '1px solid rgba(255,255,255,0.1)', position: 'relative', overflow: 'hidden' }}>
                      <div style={{ position: 'absolute', top: '-20px', right: '-20px', opacity: 0.1 }}>
                        <CreditCard size={120} />
                      </div>
                      <div style={{ fontSize: '1.25rem', fontWeight: 600, marginBottom: '24px', letterSpacing: '2px' }}>
                        **** **** **** {c.cardNumber.slice(-4)}
                      </div>
                      <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
                        <div>
                          <div style={{ fontSize: '0.75rem', marginBottom: '4px' }}>Card Holder</div>
                          <div style={{ color: 'white', fontWeight: 500 }}>{c.cardHolder}</div>
                        </div>
                        <div style={{ textAlign: 'right' }}>
                          <div style={{ fontSize: '0.75rem', marginBottom: '4px' }}>Expires</div>
                          <div style={{ color: 'white', fontWeight: 500 }}>{c.expiryDate}</div>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </>
          ) : activeTab === 'admin-fleet' && user.isAdmin ? (
            <>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px' }}>
                <h2 style={{ margin: 0 }}>Fleet Management</h2>
                <button className="btn-primary flex-center" onClick={() => { setSelectedCarToEdit(null); setIsAdminCarModalOpen(true); }} style={{ padding: '8px 16px', fontSize: '0.875rem' }}>
                  <Plus size={16} style={{ marginRight: '8px' }} /> Add New Car
                </button>
              </div>
              
              {loading ? (
                <div style={{ textAlign: 'center', padding: '40px', color: 'var(--text-secondary)' }}>
                  <Loader className="animate-spin" style={{ margin: '0 auto 16px' }} />
                  Loading fleet data...
                </div>
              ) : (
                <div style={{ overflowX: 'auto' }}>
                  <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
                    <thead>
                      <tr style={{ borderBottom: '1px solid var(--border-color)', color: 'var(--text-secondary)' }}>
                        <th style={{ padding: '12px' }}>ID</th>
                        <th style={{ padding: '12px' }}>Make/Model</th>
                        <th style={{ padding: '12px' }}>License Plate</th>
                        <th style={{ padding: '12px' }}>Daily Rate</th>
                        <th style={{ padding: '12px' }}>Status</th>
                        <th style={{ padding: '12px', textAlign: 'right' }}>Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      {allCars.map(car => (
                        <tr key={car.id} style={{ borderBottom: '1px solid rgba(255,255,255,0.05)' }}>
                          <td style={{ padding: '12px' }}>{car.id}</td>
                          <td style={{ padding: '12px' }}>{car.make} {car.model} ({car.year})</td>
                          <td style={{ padding: '12px' }}>{car.licensePlate}</td>
                          <td style={{ padding: '12px' }}>${car.dailyRate.toFixed(2)}</td>
                          <td style={{ padding: '12px' }}>
                            <span style={{ padding: '4px 8px', borderRadius: '4px', fontSize: '0.75rem', background: car.status === 'available' ? 'rgba(74, 222, 128, 0.1)' : 'rgba(239, 68, 68, 0.1)', color: car.status === 'available' ? '#4ade80' : '#ef4444' }}>
                              {car.status}
                            </span>
                          </td>
                          <td style={{ padding: '12px', textAlign: 'right' }}>
                            <button onClick={() => { setSelectedCarToEdit(car); setIsAdminCarModalOpen(true); }} style={{ background: 'transparent', border: 'none', color: 'var(--accent-blue)', cursor: 'pointer', marginRight: '16px' }}>
                              <Edit2 size={16} />
                            </button>
                            <button onClick={() => handleDeleteCar(car.id)} style={{ background: 'transparent', border: 'none', color: '#ef4444', cursor: 'pointer' }}>
                              <Trash2 size={16} />
                            </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </>
          ) : activeTab === 'admin-reservations' && user.isAdmin ? (
            <>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px' }}>
                <h2 style={{ margin: 0 }}>Global Reservations</h2>
              </div>
              
              {loading ? (
                <div style={{ textAlign: 'center', padding: '40px', color: 'var(--text-secondary)' }}>
                  <Loader className="animate-spin" style={{ margin: '0 auto 16px' }} />
                  Loading global reservations...
                </div>
              ) : (
                <div style={{ overflowX: 'auto' }}>
                  <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
                    <thead>
                      <tr style={{ borderBottom: '1px solid var(--border-color)', color: 'var(--text-secondary)' }}>
                        <th style={{ padding: '12px' }}>ID</th>
                        <th style={{ padding: '12px' }}>User ID</th>
                        <th style={{ padding: '12px' }}>Car ID</th>
                        <th style={{ padding: '12px' }}>Dates</th>
                        <th style={{ padding: '12px' }}>Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      {allReservations.map(res => (
                        <tr key={res.reservationId} style={{ borderBottom: '1px solid rgba(255,255,255,0.05)' }}>
                          <td style={{ padding: '12px' }}>{res.reservationId}</td>
                          <td style={{ padding: '12px' }}>{res.userId}</td>
                          <td style={{ padding: '12px' }}>{res.carId}</td>
                          <td style={{ padding: '12px' }}>{res.startDate} to {res.endDate}</td>
                          <td style={{ padding: '12px' }}>
                            <span style={{ padding: '4px 8px', borderRadius: '4px', fontSize: '0.75rem', background: res.status === 'confirmed' ? 'rgba(74, 222, 128, 0.1)' : 'rgba(245, 158, 11, 0.1)', color: res.status === 'confirmed' ? '#4ade80' : '#f59e0b' }}>
                              {res.status}
                            </span>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </>
          ) : null}
        </div>

      </div>

      <PaymentModal 
        isOpen={!!selectedReservationToPay} 
        onClose={() => setSelectedReservationToPay(null)} 
        reservation={selectedReservationToPay?.reservationId !== 'NEW_CARD_ONLY' ? selectedReservationToPay : null} 
        user={user} 
        onPaymentSuccess={() => {
          alert('Payment Successful!');
          loadData();
        }} 
      />

      {isAdminCarModalOpen && (
        <AdminCarModal
          isOpen={isAdminCarModalOpen}
          onClose={() => setIsAdminCarModalOpen(false)}
          car={selectedCarToEdit}
          onCarSaved={loadData}
        />
      )}
    </div>
  );
};

export default Dashboard;
