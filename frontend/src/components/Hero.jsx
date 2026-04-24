import React from 'react';
import { Calendar, MapPin, ArrowRight, Car } from 'lucide-react';

const Hero = () => {
  return (
    <div style={{
      position: 'relative',
      minHeight: '100vh',
      display: 'flex',
      alignItems: 'center',
      paddingTop: '80px',
      overflow: 'hidden'
    }}>
      {/* Background Decorators */}
      <div style={{
        position: 'absolute',
        top: '20%',
        right: '-10%',
        width: '600px',
        height: '600px',
        background: 'radial-gradient(circle, rgba(59, 130, 246, 0.15) 0%, transparent 60%)',
        borderRadius: '50%',
        zIndex: -1
      }}></div>

      <div className="container">
        <div style={{
          display: 'grid',
          gridTemplateColumns: '1fr 1fr',
          gap: '40px',
          alignItems: 'center'
        }}>
          {/* Left Content */}
          <div className="animate-fade-in">
            <div style={{ display: 'inline-block', padding: '6px 16px', background: 'rgba(59, 130, 246, 0.1)', border: '1px solid rgba(59, 130, 246, 0.2)', borderRadius: '20px', color: 'var(--accent-blue)', fontWeight: 600, fontSize: '0.875rem', marginBottom: '24px' }}>
              PREMIUM CAR RENTAL
            </div>
            <h1 style={{ fontSize: '4.5rem', lineHeight: 1.1, marginBottom: '24px' }}>
              Drive Your <br />
              <span className="text-gradient">Dream Car</span> Today.
            </h1>
            <p style={{ fontSize: '1.125rem', color: 'var(--text-secondary)', marginBottom: '40px', maxWidth: '480px', lineHeight: 1.6 }}>
              Experience the thrill of the open road with our exclusive collection of luxury and performance vehicles. Your journey starts here.
            </p>

            {/* Quick Search Widget */}
            <div className="glass-panel delay-200" style={{ padding: '24px', display: 'flex', gap: '16px', borderRadius: '20px' }}>
              <div style={{ flex: 1 }}>
                <label style={{ display: 'block', fontSize: '0.75rem', color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase', letterSpacing: '1px' }}>Pick-up Location</label>
                <div style={{ display: 'flex', alignItems: 'center', background: 'rgba(0,0,0,0.2)', padding: '12px 16px', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.05)' }}>
                  <MapPin size={18} color="var(--accent-blue)" style={{ marginRight: '10px' }} />
                  <input type="text" placeholder="City, Airport, or Address" style={{ background: 'transparent', border: 'none', color: 'white', width: '100%', outline: 'none', fontFamily: 'var(--font-sans)' }} />
                </div>
              </div>
              <div style={{ flex: 1 }}>
                <label style={{ display: 'block', fontSize: '0.75rem', color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase', letterSpacing: '1px' }}>Pick-up Date</label>
                <div style={{ display: 'flex', alignItems: 'center', background: 'rgba(0,0,0,0.2)', padding: '12px 16px', borderRadius: '12px', border: '1px solid rgba(255,255,255,0.05)' }}>
                  <Calendar size={18} color="var(--accent-purple)" style={{ marginRight: '10px' }} />
                  <input type="text" placeholder="Select Date" style={{ background: 'transparent', border: 'none', color: 'white', width: '100%', outline: 'none', fontFamily: 'var(--font-sans)' }} />
                </div>
              </div>
              <div style={{ display: 'flex', alignItems: 'flex-end' }}>
                <button className="btn-primary flex-center" style={{ height: '44px', width: '44px', borderRadius: '12px', padding: 0 }}>
                  <ArrowRight size={20} />
                </button>
              </div>
            </div>
          </div>

          {/* Right Image */}
          <div className="animate-fade-in delay-300" style={{ position: 'relative' }}>
            <img 
              src="https://images.unsplash.com/photo-1617788138017-80ad40651399?q=80&w=2070&auto=format&fit=crop" 
              alt="Luxury Car" 
              style={{ width: '100%', borderRadius: '30px', boxShadow: '0 20px 40px rgba(0,0,0,0.4)', filter: 'brightness(0.9)', border: '1px solid rgba(255,255,255,0.1)' }} 
            />
            {/* Floating Badge */}
            <div className="glass-panel" style={{ position: 'absolute', bottom: '-20px', left: '-20px', padding: '16px 24px', display: 'flex', alignItems: 'center', gap: '16px', borderRadius: '16px' }}>
              <div style={{ background: 'rgba(59, 130, 246, 0.2)', padding: '12px', borderRadius: '12px' }}>
                <Car size={24} color="var(--accent-blue)" />
              </div>
              <div>
                <p style={{ fontWeight: 700, fontSize: '1.25rem', margin: 0 }}>200+</p>
                <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem', margin: 0 }}>Premium Cars</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Hero;
