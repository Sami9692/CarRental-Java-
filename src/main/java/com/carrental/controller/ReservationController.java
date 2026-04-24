package com.carrental.controller;

import com.carrental.dao.InsuranceDAO;
import com.carrental.dao.impl.InsuranceDAOImpl;
import com.carrental.model.InsuranceType;
import com.carrental.model.Reservation;
import com.carrental.service.impl.ReservationServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Controller for reservation-related UI actions.
 */
public class ReservationController {

    private final ReservationServiceImpl reservationService;
    private final InsuranceDAO insuranceDAO;
    private final Scanner scanner;

    public ReservationController(Scanner scanner) {
        this.reservationService = new ReservationServiceImpl();
        this.insuranceDAO       = new InsuranceDAOImpl();
        this.scanner            = scanner;
    }

    /** Book a car for the logged-in user. */
    public void bookCar(int userId) {
        System.out.println("\n=== BOOK A CAR ===");
        try {
            System.out.print("Car ID          : "); int carId   = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Location ID     : "); int locId   = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Start Date      : "); LocalDate s = LocalDate.parse(scanner.nextLine().trim());
            System.out.print("End Date        : "); LocalDate e = LocalDate.parse(scanner.nextLine().trim());

            // Show insurance options
            System.out.println("\nAvailable Insurance Plans:");
            List<InsuranceType> types = insuranceDAO.findAllTypes();
            types.forEach(t -> System.out.printf("  [%d] %s – %s%n",
                    t.getInsuranceTypeId(), t.getTypeName(), t.getCoverage()));

            System.out.print("Insurance Type ID: "); int insId = Integer.parseInt(scanner.nextLine().trim());

            // Preview cost
            BigDecimal total = reservationService.calculateTotalCost(carId, insId, s, e);
            System.out.printf("%nEstimated Total: $%.2f%n", total);
            System.out.print("Confirm booking? (yes/no): ");
            if (!"yes".equalsIgnoreCase(scanner.nextLine().trim())) {
                System.out.println("Booking cancelled.");
                return;
            }

            Reservation res = reservationService.bookCar(userId, carId, locId, s, e, insId);
            System.out.printf("✔ Reservation confirmed! ID: %d%n", res.getReservationId());
        } catch (DateTimeParseException ex) {
            System.out.println("✘ Invalid date format. Use yyyy-MM-dd.");
        } catch (NumberFormatException ex) {
            System.out.println("✘ Invalid number input.");
        } catch (Exception ex) {
            System.out.println("✘ " + ex.getMessage());
        }
    }

    /** Cancel one of the logged-in user's reservations. */
    public void cancelReservation(int userId) {
        System.out.println("\n=== CANCEL RESERVATION ===");
        viewMyReservations(userId);
        System.out.print("Reservation ID to cancel: ");
        try {
            int resId = Integer.parseInt(scanner.nextLine().trim());
            reservationService.cancelReservation(resId, userId);
            System.out.println("✔ Reservation " + resId + " cancelled.");
        } catch (Exception ex) {
            System.out.println("✘ " + ex.getMessage());
        }
    }

    /** Show reservations for the current user. */
    public void viewMyReservations(int userId) {
        List<Reservation> list = reservationService.getUserReservations(userId);
        System.out.println("\n=== MY RESERVATIONS ===");
        if (list.isEmpty()) { System.out.println("No reservations found."); return; }
        printReservationTable(list);
    }

    /** Admin: view all reservations. */
    public void viewAllReservations() {
        List<Reservation> list = reservationService.getAllReservations();
        System.out.println("\n=== ALL RESERVATIONS ===");
        if (list.isEmpty()) { System.out.println("No reservations found."); return; }
        printReservationTable(list);
    }

    /** Check availability without booking. */
    public void checkAvailability() {
        System.out.println("\n=== CHECK CAR AVAILABILITY ===");
        try {
            System.out.print("Car ID      : "); int carId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Start Date  : "); LocalDate s = LocalDate.parse(scanner.nextLine().trim());
            System.out.print("End Date    : "); LocalDate e = LocalDate.parse(scanner.nextLine().trim());
            boolean available = reservationService.checkAvailability(carId, s, e);
            System.out.println(available
                    ? "✔ Car is AVAILABLE for those dates."
                    : "✘ Car is NOT available for those dates.");
        } catch (DateTimeParseException ex) {
            System.out.println("✘ Invalid date format. Use yyyy-MM-dd.");
        } catch (NumberFormatException ex) {
            System.out.println("✘ Invalid number.");
        }
    }

    private void printReservationTable(List<Reservation> list) {
        System.out.printf("%-6s %-8s %-8s %-12s %-12s %-12s%n",
                "ID", "CarID", "UserID", "StartDate", "EndDate", "Status");
        System.out.println("-".repeat(65));
        for (Reservation r : list) {
            System.out.printf("%-6d %-8d %-8d %-12s %-12s %-12s%n",
                    r.getReservationId(), r.getCarId(), r.getUserId(),
                    r.getStartDate(), r.getEndDate(), r.getStatus());
        }
    }
}
