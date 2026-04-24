package com.carrental.controller;

import com.carrental.model.Car;
import com.carrental.service.impl.CarServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Controller for car-related UI actions.
 */
public class CarController {

    private final CarServiceImpl carService;
    private final Scanner scanner;

    public CarController(Scanner scanner) {
        this.carService = new CarServiceImpl();
        this.scanner    = scanner;
    }

    public void listAllCars() {
        System.out.println("\n=== ALL CARS ===");
        List<Car> cars = carService.getAllCars();
        if (cars.isEmpty()) { System.out.println("No cars registered."); return; }
        printCarTable(cars);
    }

    public void listAvailableCars() {
        System.out.println("\n=== AVAILABLE CARS ===");
        List<Car> cars = carService.getAvailableCars();
        if (cars.isEmpty()) { System.out.println("No cars currently available."); return; }
        printCarTable(cars);
    }

    public void searchAvailableByDates() {
        System.out.println("\n=== SEARCH CARS BY DATES ===");
        try {
            System.out.print("Start Date (yyyy-MM-dd): "); LocalDate s = LocalDate.parse(scanner.nextLine().trim());
            System.out.print("End Date   (yyyy-MM-dd): "); LocalDate e = LocalDate.parse(scanner.nextLine().trim());
            List<Car> cars = carService.getAvailableCarsByDates(s, e);
            if (cars.isEmpty()) System.out.println("No cars available for those dates.");
            else printCarTable(cars);
        } catch (DateTimeParseException ex) {
            System.out.println("✘ Invalid date format. Use yyyy-MM-dd.");
        } catch (IllegalArgumentException ex) {
            System.out.println("✘ " + ex.getMessage());
        }
    }

    /** Admin: add a new car. */
    public void addCar() {
        System.out.println("\n=== ADD CAR ===");
        try {
            System.out.print("Make        : "); String make  = scanner.nextLine().trim();
            System.out.print("Model       : "); String model = scanner.nextLine().trim();
            System.out.print("Year        : "); int year     = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Plate       : "); String plate = scanner.nextLine().trim();
            System.out.print("Daily Rate  : "); BigDecimal rate = new BigDecimal(scanner.nextLine().trim());
            System.out.print("Type ID     : "); int typeId  = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Location ID : "); int locId   = Integer.parseInt(scanner.nextLine().trim());

            Car car = carService.addCar(make, model, year, plate, rate, typeId, locId, "");
            System.out.println("✔ Car added: " + car);
        } catch (NumberFormatException e) {
            System.out.println("✘ Invalid number input.");
        } catch (Exception e) {
            System.out.println("✘ Error: " + e.getMessage());
        }
    }

    /** Admin: update car details. */
    public void updateCar() {
        System.out.println("\n=== UPDATE CAR ===");
        try {
            System.out.print("Car ID      : "); int carId = Integer.parseInt(scanner.nextLine().trim());
            Car car = carService.getCarById(carId)
                    .orElseThrow(() -> new IllegalArgumentException("Car not found: " + carId));

            System.out.printf("Current: %s%n", car);
            System.out.print("New Make   (blank=keep): "); String make  = scanner.nextLine().trim();
            System.out.print("New Model  (blank=keep): "); String model = scanner.nextLine().trim();
            System.out.print("New Rate   (blank=keep): "); String rate  = scanner.nextLine().trim();
            System.out.print("New Status (blank=keep): "); String status= scanner.nextLine().trim();

            if (!make.isEmpty())   car.setMake(make);
            if (!model.isEmpty())  car.setModel(model);
            if (!rate.isEmpty())   car.setDailyRate(new BigDecimal(rate));
            if (!status.isEmpty()) car.setStatus(status);

            carService.updateCar(car);
            System.out.println("✔ Car updated.");
        } catch (Exception e) {
            System.out.println("✘ Error: " + e.getMessage());
        }
    }

    /** Admin: delete a car. */
    public void deleteCar() {
        System.out.println("\n=== DELETE CAR ===");
        System.out.print("Car ID to delete: ");
        try {
            int carId = Integer.parseInt(scanner.nextLine().trim());
            carService.deleteCar(carId);
            System.out.println("✔ Car " + carId + " deleted.");
        } catch (Exception e) {
            System.out.println("✘ Error: " + e.getMessage());
        }
    }

    private void printCarTable(List<Car> cars) {
        System.out.printf("%-5s %-10s %-15s %-5s %-12s %-12s %-10s%n",
                "ID", "Make", "Model", "Year", "Plate", "Rate/Day", "Status");
        System.out.println("-".repeat(75));
        for (Car c : cars) {
            System.out.printf("%-5d %-10s %-15s %-5d %-12s $%-11.2f %-10s%n",
                    c.getCarId(), c.getMake(), c.getModel(), c.getYear(),
                    c.getLicensePlate(), c.getDailyRate(), c.getStatus());
        }
    }
}
