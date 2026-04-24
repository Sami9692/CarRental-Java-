package com.carrental.service.impl;

import com.carrental.dao.CarDAO;
import com.carrental.dao.impl.CarDAOImpl;
import com.carrental.model.Car;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for car management.
 */
public class CarServiceImpl {

    private final CarDAO carDAO;

    public CarServiceImpl() {
        this.carDAO = new CarDAOImpl();
    }

    public List<Car> getAllCars() {
        return carDAO.findAll();
    }

    public List<Car> getAvailableCars() {
        return carDAO.findAvailableCars();
    }

    public List<Car> getAvailableCarsByDates(LocalDate startDate, LocalDate endDate) {
        validateDates(startDate, endDate);
        return carDAO.findAvailableCarsByDates(startDate, endDate);
    }

    public Optional<Car> getCarById(int carId) {
        return carDAO.findById(carId);
    }

    /** Admin: Add a new car. */
    public Car addCar(String make, String model, int year, String licensePlate,
                      BigDecimal dailyRate, int typeId, int locationId, String imageUrl) {
        Car car = new Car();
        car.setMake(make);
        car.setModel(model);
        car.setYear(year);
        car.setLicensePlate(licensePlate);
        car.setStatus("available");
        car.setDailyRate(dailyRate);
        car.setTypeId(typeId);
        car.setLocationId(locationId);
        car.setImageUrl(imageUrl);
        carDAO.addCar(car);
        return car;
    }

    /** Admin: Update car details. */
    public void updateCar(Car car) {
        if (carDAO.findById(car.getCarId()).isEmpty()) {
            throw new IllegalArgumentException("Car not found: " + car.getCarId());
        }
        carDAO.updateCar(car);
    }

    /** Admin: Remove a car. */
    public void deleteCar(int carId) {
        if (carDAO.findById(carId).isEmpty()) {
            throw new IllegalArgumentException("Car not found: " + carId);
        }
        carDAO.deleteCar(carId);
    }

    public void setCarStatus(int carId, String status) {
        carDAO.updateCarStatus(carId, status);
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
        if (start.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past.");
        }
    }
}
