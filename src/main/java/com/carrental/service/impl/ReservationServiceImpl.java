package com.carrental.service.impl;

import com.carrental.dao.CarDAO;
import com.carrental.dao.InsuranceDAO;
import com.carrental.dao.ReservationDAO;
import com.carrental.dao.impl.CarDAOImpl;
import com.carrental.dao.impl.InsuranceDAOImpl;
import com.carrental.dao.impl.ReservationDAOImpl;
import com.carrental.model.Car;
import com.carrental.model.InsurancePrice;
import com.carrental.model.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Handles reservation business logic including overlap checks and cost calculation.
 *
 * Cost formula: (car.dailyRate + insurance.dailyPrice) * numberOfDays
 */
public class ReservationServiceImpl {

    private final ReservationDAO reservationDAO;
    private final CarDAO carDAO;
    private final InsuranceDAO insuranceDAO;

    public ReservationServiceImpl() {
        this.reservationDAO = new ReservationDAOImpl();
        this.carDAO         = new CarDAOImpl();
        this.insuranceDAO   = new InsuranceDAOImpl();
    }

    /**
     * Creates a reservation after checking for date overlap and car existence.
     * Sets car status to 'rented' if start date is today.
     *
     * @return the new Reservation with its generated ID
     */
    public Reservation bookCar(int userId, int carId, int locationId,
                                LocalDate startDate, LocalDate endDate, int insuranceTypeId) {

        validateDates(startDate, endDate);

        // Check car exists
        Car car = carDAO.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found: " + carId));

        if ("maintenance".equalsIgnoreCase(car.getStatus())) {
            throw new IllegalStateException("Car is currently under maintenance.");
        }

        // Overlap check
        if (!reservationDAO.isCarAvailable(carId, startDate, endDate)) {
            throw new IllegalStateException(
                "Car " + carId + " is not available from " + startDate + " to " + endDate);
        }

        Reservation res = new Reservation();
        res.setUserId(userId);
        res.setCarId(carId);
        res.setLocationId(locationId);
        res.setStartDate(startDate);
        res.setEndDate(endDate);
        res.setStatus("pending");
        res.setInsuranceTypeId(insuranceTypeId);

        reservationDAO.createReservation(res);

        // Auto-update car status if rental starts today
        if (!startDate.isAfter(LocalDate.now())) {
            carDAO.updateCarStatus(carId, "rented");
        }

        System.out.printf("[ReservationService] Booked: %s%n", res);
        return res;
    }

    /**
     * Cancels a reservation and frees the car if no other active reservations exist for it.
     */
    public void cancelReservation(int reservationId, int requestingUserId) {
        Reservation res = reservationDAO.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservationId));

        // Only the owning user or admin can cancel
        if (res.getUserId() != requestingUserId) {
            throw new SecurityException("You do not own this reservation.");
        }

        if ("cancelled".equalsIgnoreCase(res.getStatus())) {
            throw new IllegalStateException("Reservation is already cancelled.");
        }

        reservationDAO.cancelReservation(reservationId);

        // Check if the car has any other active reservation; if not, set status back to 'available'
        boolean hasOtherActiveRes = reservationDAO.findAll().stream()
                .anyMatch(r -> r.getCarId() == res.getCarId()
                            && r.getReservationId() != reservationId
                            && !"cancelled".equalsIgnoreCase(r.getStatus()));

        if (!hasOtherActiveRes) {
            carDAO.updateCarStatus(res.getCarId(), "available");
        }

        System.out.println("[ReservationService] Cancelled reservation " + reservationId);
    }

    public List<Reservation> getUserReservations(int userId) {
        return reservationDAO.findByUserId(userId);
    }

    public List<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }

    public Optional<Reservation> getReservationById(int reservationId) {
        return reservationDAO.findById(reservationId);
    }

    /**
     * Calculates the total cost for a reservation:
     *   totalCost = (dailyRate + insuranceDailyPrice) * numberOfDays
     */
    public BigDecimal calculateTotalCost(int carId, int insuranceTypeId,
                                          LocalDate startDate, LocalDate endDate) {
        Car car = carDAO.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found: " + carId));

        InsurancePrice insurancePrice = insuranceDAO.findPriceByTypeId(insuranceTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Insurance type not found: " + insuranceTypeId));

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days <= 0) throw new IllegalArgumentException("Invalid date range.");

        BigDecimal dailyCost = car.getDailyRate().add(insurancePrice.getDailyPrice());
        return dailyCost.multiply(BigDecimal.valueOf(days));
    }

    public boolean checkAvailability(int carId, LocalDate startDate, LocalDate endDate) {
        return reservationDAO.isCarAvailable(carId, startDate, endDate);
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null) throw new IllegalArgumentException("Dates cannot be null.");
        if (!start.isBefore(end)) throw new IllegalArgumentException("Start date must be before end date.");
        if (start.isBefore(LocalDate.now())) throw new IllegalArgumentException("Start date cannot be in the past.");
    }
}
