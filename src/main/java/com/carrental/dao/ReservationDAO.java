package com.carrental.dao;

import com.carrental.model.Reservation;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationDAO {
    int createReservation(Reservation reservation);   // returns generated reservationId
    Optional<Reservation> findById(int reservationId);
    List<Reservation> findByUserId(int userId);
    List<Reservation> findAll();
    void updateStatus(int reservationId, String status);
    void cancelReservation(int reservationId);
    boolean isCarAvailable(int carId, LocalDate startDate, LocalDate endDate);
    boolean isCarAvailableExcluding(int carId, LocalDate startDate, LocalDate endDate, int excludeReservationId);
}
