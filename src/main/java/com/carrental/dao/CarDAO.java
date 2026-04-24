package com.carrental.dao;

import com.carrental.model.Car;
import java.util.List;
import java.util.Optional;

/**
 * Abstraction layer for Car persistence operations.
 * Follows the DAO (Data Access Object) pattern.
 */
public interface CarDAO {
    void addCar(Car car);
    void updateCar(Car car);
    void deleteCar(int carId);
    Optional<Car> findById(int carId);
    List<Car> findAll();
    List<Car> findAvailableCars();
    List<Car> findAvailableCarsByDates(java.time.LocalDate startDate, java.time.LocalDate endDate);
    void updateCarStatus(int carId, String status);
}
