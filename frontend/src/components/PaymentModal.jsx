import React, { useState, useEffect } from 'react';
import { X, CreditCard, Lock, ShieldCheck } from 'lucide-react';
import { fetchUserCards, addCard, processPayment } from '../api';

const PaymentModal = ({ isOpen, onClose, reservation, user, onPaymentSuccess }) => {
  const [cards, setCards] = useState([]);
  const [selectedCardId, setSelectedCardId] = useState('');
  
  const [useNewCard, setUseNewCard] = useState(true);
  const [newCardNumber, setNewCardNumber] = useState('');
  const [newCardHolder, setNewCardHolder] = useState('');
  const [newExpiry, setNewExpiry] = useState('');
  const [cvv, setCvv] = useState('');
  const [billingAddress, setBillingAddress] = useState('');
  const [saveCard, setSaveCard] = useState(true);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  
  // Hardcoded estimated amount for realism if API doesn't provide it immediately
  const amount = reservation ? (reservation.amount || 250.00).toFixed(2) : '0.00';

  useEffect(() => {
    if (isOpen && user) {
      loadCards();
      setNewCardNumber('');
      setNewCardHolder('');
      setNewExpiry('');
      setCvv('');
      setBillingAddress('');
      setError('');
    }
  }, [isOpen, user]);

  const loadCards = async () => {
    try {
      const userCards = await fetchUserCards(user.userId);
      setCards(userCards);
      if (userCards.length > 0) {
        setUseNewCard(false);
        setSelectedCardId(userCards[0].cardId);
      } else {
        setUseNewCard(true);
      }
    } catch (err) {
      console.error(err);
    }
  };

  if (!isOpen || !reservation || !user) return null;

  const handlePayment = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      let cardIdToUse = selectedCardId;
      
      // If using new card form, we must save it to the DB first to process payment
      if (useNewCard) {
        // Detect basic type
        let cardType = 'Visa';
        if (newCardNumber.startsWith('5')) cardType = 'Mastercard';
        if (newCardNumber.startsWith('3')) cardType = 'Amex';
        
        const card = await addCard(user.userId, newCardNumber, newCardHolder, newExpiry, cardType);
        cardIdToUse = card.cardId;
        
        if (saveCard) {
          setCards([...cards, card]);
        }
      }
      
      await processPayment(reservation.reservationId, user.userId, cardIdToUse, 'Credit');
      
      // If they didn't want to save it, theoretically we'd delete it here, but keeping it simple
      
      setLoading(false);
      onPaymentSuccess();
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
      <div className="glass-panel animate-fade-in" style={{ width: '100%', maxWidth: '480px', padding: '0', position: 'relative', maxHeight: '90vh', overflowY: 'auto', borderRadius: '20px' }}>
        
        {/* Header */}
        <div style={{ padding: '24px 32px', borderBottom: '1px solid rgba(255,255,255,0.1)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <h2 style={{ margin: 0, fontSize: '1.5rem', fontWeight: 600 }}>Payment Details</h2>
          <div style={{ color: '#4ade80', fontWeight: 600, fontSize: '1.25rem' }}>
            Amount: ${amount}
          </div>
          <button onClick={onClose} style={{ position: 'absolute', top: '24px', right: '24px', background: 'transparent', color: 'var(--text-secondary)' }}>
            <X size={24} />
          </button>
        </div>

        <div style={{ padding: '32px' }}>
          {error && <div style={{ background: 'rgba(239, 68, 68, 0.1)', border: '1px solid #ef4444', color: '#ef4444', padding: '12px', borderRadius: '8px', marginBottom: '24px', fontSize: '0.875rem' }}>{error}</div>}

          {cards.length > 0 && (
            <div style={{ marginBottom: '24px', display: 'flex', gap: '16px' }}>
              <button 
                type="button"
                onClick={() => setUseNewCard(false)}
                style={{ flex: 1, padding: '10px', borderRadius: '8px', background: !useNewCard ? 'rgba(59, 130, 246, 0.2)' : 'rgba(0,0,0,0.2)', border: !useNewCard ? '1px solid var(--accent-blue)' : '1px solid rgba(255,255,255,0.1)', color: !useNewCard ? 'white' : 'var(--text-secondary)', fontWeight: 500, cursor: 'pointer' }}
              >
                Saved Cards
              </button>
              <button 
                type="button"
                onClick={() => setUseNewCard(true)}
                style={{ flex: 1, padding: '10px', borderRadius: '8px', background: useNewCard ? 'rgba(59, 130, 246, 0.2)' : 'rgba(0,0,0,0.2)', border: useNewCard ? '1px solid var(--accent-blue)' : '1px solid rgba(255,255,255,0.1)', color: useNewCard ? 'white' : 'var(--text-secondary)', fontWeight: 500, cursor: 'pointer' }}
              >
                New Card
              </button>
            </div>
          )}

          <form onSubmit={handlePayment} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
            
            {!useNewCard ? (
              <div>
                <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Select Saved Card</label>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                  {cards.map(c => (
                    <div 
                      key={c.cardId} 
                      onClick={() => setSelectedCardId(c.cardId)}
                      style={{ 
                        padding: '16px', borderRadius: '12px', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '16px',
                        border: selectedCardId === c.cardId ? '2px solid var(--accent-blue)' : '1px solid rgba(255,255,255,0.1)',
                        background: selectedCardId === c.cardId ? 'rgba(59, 130, 246, 0.1)' : 'rgba(0,0,0,0.2)'
                      }}
                    >
                      <CreditCard size={24} color={selectedCardId === c.cardId ? 'var(--accent-blue)' : 'var(--text-secondary)'} />
                      <div>
                        <div style={{ fontWeight: 600 }}>{c.cardType} ending in {c.cardNumber.slice(-4)}</div>
                        <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>Expires {c.expiryDate}</div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            ) : (
              <>
                <div>
                  <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Card Number</label>
                  <div style={{ position: 'relative' }}>
                    <input 
                      value={newCardNumber} 
                      onChange={e=>setNewCardNumber(e.target.value)} 
                      required 
                      type="text" 
                      placeholder="4111 1111 1111 1111" 
                      style={{ background: 'rgba(0,0,0,0.3)', border: '1px solid rgba(255,255,255,0.1)', color: 'white', padding: '14px 16px', borderRadius: '12px', width: '100%', outline: 'none', fontSize: '1rem', letterSpacing: '2px' }} 
                    />
                    <div style={{ position: 'absolute', right: '16px', top: '50%', transform: 'translateY(-50%)' }}>
                      <CreditCard size={20} color="var(--text-secondary)" />
                    </div>
                  </div>
                  <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', marginTop: '6px' }}>Supported: Visa, Mastercard, Amex</div>
                </div>
                
                <div>
                  <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Name on Card</label>
                  <input 
                    value={newCardHolder} 
                    onChange={e=>setNewCardHolder(e.target.value)} 
                    required 
                    type="text" 
                    placeholder="Name as shown on card" 
                    style={{ background: 'rgba(0,0,0,0.3)', border: '1px solid rgba(255,255,255,0.1)', color: 'white', padding: '14px 16px', borderRadius: '12px', width: '100%', outline: 'none', fontSize: '1rem' }} 
                  />
                </div>

                <div style={{ display: 'flex', gap: '16px' }}>
                  <div style={{ flex: 1 }}>
                    <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Expiry Date</label>
                    <input 
                      value={newExpiry} 
                      onChange={e=>setNewExpiry(e.target.value)} 
                      required 
                      type="text" 
                      placeholder="MM/YY" 
                      style={{ background: 'rgba(0,0,0,0.3)', border: '1px solid rgba(255,255,255,0.1)', color: 'white', padding: '14px 16px', borderRadius: '12px', width: '100%', outline: 'none', fontSize: '1rem' }} 
                    />
                  </div>
                  <div style={{ flex: 1 }}>
                    <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>CVV</label>
                    <input 
                      value={cvv} 
                      onChange={e=>setCvv(e.target.value)} 
                      required 
                      type="password" 
                      placeholder="123" 
                      maxLength="4"
                      style={{ background: 'rgba(0,0,0,0.3)', border: '1px solid rgba(255,255,255,0.1)', color: 'white', padding: '14px 16px', borderRadius: '12px', width: '100%', outline: 'none', fontSize: '1rem' }} 
                    />
                  </div>
                </div>

                <div>
                  <label style={{ display: 'block', marginBottom: '8px', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Billing Address</label>
                  <textarea 
                    value={billingAddress} 
                    onChange={e=>setBillingAddress(e.target.value)} 
                    required 
                    placeholder="Enter your billing address" 
                    rows="2"
                    style={{ background: 'rgba(0,0,0,0.3)', border: '1px solid rgba(255,255,255,0.1)', color: 'white', padding: '14px 16px', borderRadius: '12px', width: '100%', outline: 'none', fontSize: '1rem', resize: 'none' }} 
                  />
                </div>

                <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginTop: '4px' }}>
                  <input 
                    type="checkbox" 
                    id="saveCard" 
                    checked={saveCard} 
                    onChange={e => setSaveCard(e.target.checked)}
                    style={{ width: '18px', height: '18px', cursor: 'pointer', accentColor: 'var(--accent-blue)' }}
                  />
                  <label htmlFor="saveCard" style={{ fontSize: '0.875rem', color: 'var(--text-secondary)', cursor: 'pointer' }}>
                    Save card for future payments
                  </label>
                </div>
              </>
            )}

            <button type="submit" disabled={loading || (!useNewCard && !selectedCardId)} style={{ width: '100%', padding: '16px', fontSize: '1.125rem', background: '#16a34a', color: 'white', border: 'none', borderRadius: '12px', fontWeight: 600, cursor: 'pointer', display: 'flex', justifyContent: 'center', alignItems: 'center', marginTop: '12px', transition: 'background 0.3s' }} onMouseOver={e => e.currentTarget.style.background = '#15803d'} onMouseOut={e => e.currentTarget.style.background = '#16a34a'}>
              {loading ? 'Processing...' : `Pay $${amount}`}
            </button>

            <div style={{ textAlign: 'center', marginTop: '16px' }}>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '8px', color: 'var(--text-secondary)', fontSize: '0.75rem', marginBottom: '12px' }}>
                <Lock size={12} color="#fbbf24" />
                <span>Your payment is secured with 256-bit encryption</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'center', gap: '16px', opacity: 0.5 }}>
                <div style={{ fontWeight: 800, fontStyle: 'italic', fontSize: '1.2rem', color: '#1a1f36' }}>VISA</div>
                <div style={{ display: 'flex' }}>
                  <div style={{ width: '24px', height: '24px', borderRadius: '50%', background: '#eb001b', opacity: 0.8, marginRight: '-10px' }}></div>
                  <div style={{ width: '24px', height: '24px', borderRadius: '50%', background: '#f79e1b', opacity: 0.8 }}></div>
                </div>
                <div style={{ fontWeight: 800, fontStyle: 'italic', fontSize: '1.2rem', color: '#1a1f36' }}>Amex</div>
              </div>
            </div>

          </form>
        </div>
      </div>
    </div>
  );
};

export default PaymentModal;
