const API_URL = 'http://localhost:8080/api';

export const login = async (username, password) => {
  const res = await fetch(`${API_URL}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  if (!res.ok) throw new Error('Invalid credentials');
  return res.json();
};

export const register = async (userData) => {
  const res = await fetch(`${API_URL}/auth/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData)
  });
  if (!res.ok) {
    const data = await res.json();
    throw new Error(data.error || 'Registration failed');
  }
  return res.json();
};

export const fetchCars = async () => {
  const res = await fetch(`${API_URL}/cars/available`);
  if (!res.ok) throw new Error('Failed to fetch cars');
  const cars = await res.json();
  
  // Transform backend Car model to Frontend model
  return cars.map(car => ({
    id: car.carId,
    make: car.make,
    model: car.model,
    year: car.year,
    type: "Premium", // Backend doesn't have string type easily accessible, defaulting
    dailyRate: car.dailyRate,
    status: car.status,
    image: car.imageUrl || `https://images.unsplash.com/photo-1494976388531-d1058494cdd8?auto=format&fit=crop&q=80&w=800`,
    features: ["Automatic", "A/C", "Bluetooth"]
  }));
};

export const fetchUserReservations = async (userId) => {
  const res = await fetch(`${API_URL}/reservations/user/${userId}`);
  if (!res.ok) throw new Error('Failed to fetch reservations');
  return res.json();
};

export const bookCar = async (userId, carId, startDate, endDate, insuranceTypeId = 1) => {
  const res = await fetch(`${API_URL}/reservations`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      userId,
      carId,
      locationId: 1, // Defaulting for now
      startDate,
      endDate,
      insuranceTypeId
    })
  });
  if (!res.ok) {
    const data = await res.json();
    throw new Error(data.error || 'Booking failed');
  }
  return res.json();
};

export const fetchUserCards = async (userId) => {
  const res = await fetch(`${API_URL}/payments/cards/user/${userId}`);
  if (!res.ok) throw new Error('Failed to fetch cards');
  return res.json();
};

export const addCard = async (userId, cardNumber, cardHolder, expiryDate, cardType) => {
  const res = await fetch(`${API_URL}/payments/cards`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      userId,
      cardNumber,
      cardHolder,
      expiryDate,
      cardType
    })
  });
  if (!res.ok) {
    const data = await res.json();
    throw new Error(data.error || 'Adding card failed');
  }
  return res.json();
};

export const processPayment = async (reservationId, userId, cardId, paymentMethod) => {
  const res = await fetch(`${API_URL}/payments`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      reservationId,
      userId,
      cardId,
      paymentMethod
    })
  });
  if (!res.ok) {
    const data = await res.json();
    throw new Error(data.error || 'Payment failed');
  }
  return res.json();
};

export const fetchAllCars = async () => {
  const res = await fetch(`${API_URL}/cars`);
  if (!res.ok) throw new Error('Failed to fetch all cars');
  const cars = await res.json();
  return cars.map(car => ({
    id: car.carId,
    make: car.make,
    model: car.model,
    year: car.year,
    licensePlate: car.licensePlate,
    type: "Premium", 
    typeId: car.typeId,
    locationId: car.locationId,
    dailyRate: car.dailyRate,
    status: car.status,
    image: car.imageUrl || `https://images.unsplash.com/photo-1494976388531-d1058494cdd8?auto=format&fit=crop&q=80&w=800`,
    features: ["Automatic", "A/C", "Bluetooth"]
  }));
};

export const addCar = async (carData) => {
  const res = await fetch(`${API_URL}/cars`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(carData)
  });
  if (!res.ok) {
    const data = await res.json();
    throw new Error(data.error || 'Failed to add car');
  }
  return res.json();
};

export const updateCar = async (carId, carData) => {
  const res = await fetch(`${API_URL}/cars/${carId}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(carData)
  });
  if (!res.ok) {
    const data = await res.json();
    throw new Error(data.error || 'Failed to update car');
  }
  return res.json();
};

export const deleteCar = async (carId) => {
  const res = await fetch(`${API_URL}/cars/${carId}`, {
    method: 'DELETE'
  });
  if (!res.ok) {
    const data = await res.json();
    throw new Error(data.error || 'Failed to delete car');
  }
  return res.json();
};

export const fetchAllReservations = async () => {
  const res = await fetch(`${API_URL}/reservations`);
  if (!res.ok) throw new Error('Failed to fetch all reservations');
  return res.json();
};
