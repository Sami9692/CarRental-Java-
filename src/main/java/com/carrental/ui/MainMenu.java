package com.carrental.ui;

import com.carrental.controller.*;
import com.carrental.model.User;
import com.carrental.util.DBConnection;

import java.util.Scanner;

/**
 * Entry point – console-based MVC menu system.
 *
 * Run with:
 *   java -cp ".:lib/mysql-connector-j-*.jar" com.carrental.ui.MainMenu
 */
public class MainMenu {

    private static final Scanner scanner = new Scanner(System.in);

    // Controllers
    private static final AuthController        authCtrl        = new AuthController(scanner);
    private static final CarController         carCtrl         = new CarController(scanner);
    private static final ReservationController reservationCtrl = new ReservationController(scanner);
    private static final PaymentController     paymentCtrl     = new PaymentController(scanner);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║   ONLINE CAR RENTAL SYSTEM       ║");
        System.out.println("║   Java + JDBC + MVC              ║");
        System.out.println("╚══════════════════════════════════╝");

        try {
            // Trigger DB connection on startup
            DBConnection.getInstance();

            boolean running = true;
            while (running) {
                if (!authCtrl.isLoggedIn()) {
                    running = showGuestMenu();
                } else if (authCtrl.isAdmin()) {
                    running = showAdminMenu();
                } else {
                    running = showUserMenu();
                }
            }
        } finally {
            DBConnection.getInstance().closeConnection();
            scanner.close();
            System.out.println("Goodbye!");
        }
    }

    // ─── GUEST MENU ───────────────────────────────────────────────────────────

    private static boolean showGuestMenu() {
        System.out.println("\n─── MAIN MENU ───────────────────");
        System.out.println("  1. Login");
        System.out.println("  2. Register");
        System.out.println("  3. Browse Available Cars");
        System.out.println("  0. Exit");
        System.out.print("Choice: ");

        switch (readInt()) {
            case 1 -> authCtrl.login();
            case 2 -> authCtrl.register();
            case 3 -> carCtrl.listAvailableCars();
            case 0 -> { return false; }
            default -> System.out.println("Invalid choice.");
        }
        return true;
    }

    // ─── USER MENU ────────────────────────────────────────────────────────────

    private static boolean showUserMenu() {
        User u = authCtrl.getCurrentUser();
        System.out.printf("%n─── USER MENU  [%s] ──────────────%n", u.getFullName());
        System.out.println("  CARS");
        System.out.println("    1. View Available Cars");
        System.out.println("    2. Search Cars by Dates");
        System.out.println("  RESERVATIONS");
        System.out.println("    3. Book a Car");
        System.out.println("    4. My Reservations");
        System.out.println("    5. Cancel Reservation");
        System.out.println("    6. Check Car Availability");
        System.out.println("  PAYMENTS");
        System.out.println("    7. Make Payment");
        System.out.println("    8. Add Payment Card");
        System.out.println("    9. Remove Card");
        System.out.println("   10. Payment History");
        System.out.println("  ACCOUNT");
        System.out.println("    0. Logout");
        System.out.print("Choice: ");

        switch (readInt()) {
            case 1  -> carCtrl.listAvailableCars();
            case 2  -> carCtrl.searchAvailableByDates();
            case 3  -> reservationCtrl.bookCar(u.getUserId());
            case 4  -> reservationCtrl.viewMyReservations(u.getUserId());
            case 5  -> reservationCtrl.cancelReservation(u.getUserId());
            case 6  -> reservationCtrl.checkAvailability();
            case 7  -> paymentCtrl.makePayment(u.getUserId());
            case 8  -> paymentCtrl.addCard(u.getUserId());
            case 9  -> paymentCtrl.deleteCard(u.getUserId());
            case 10 -> paymentCtrl.viewPaymentHistory(u.getUserId());
            case 0  -> authCtrl.logout();
            default -> System.out.println("Invalid choice.");
        }
        return true;
    }

    // ─── ADMIN MENU ───────────────────────────────────────────────────────────

    private static boolean showAdminMenu() {
        User u = authCtrl.getCurrentUser();
        System.out.printf("%n─── ADMIN MENU [%s] ─────────────%n", u.getFullName());
        System.out.println("  CAR MANAGEMENT");
        System.out.println("    1.  List All Cars");
        System.out.println("    2.  Add Car");
        System.out.println("    3.  Update Car");
        System.out.println("    4.  Delete Car");
        System.out.println("  RESERVATIONS");
        System.out.println("    5.  View All Reservations");
        System.out.println("    6.  Check Car Availability");
        System.out.println("  SEARCH");
        System.out.println("    7.  Search Available Cars by Dates");
        System.out.println("  ACCOUNT");
        System.out.println("    0.  Logout");
        System.out.print("Choice: ");

        switch (readInt()) {
            case 1  -> carCtrl.listAllCars();
            case 2  -> carCtrl.addCar();
            case 3  -> carCtrl.updateCar();
            case 4  -> carCtrl.deleteCar();
            case 5  -> reservationCtrl.viewAllReservations();
            case 6  -> reservationCtrl.checkAvailability();
            case 7  -> carCtrl.searchAvailableByDates();
            case 0  -> authCtrl.logout();
            default -> System.out.println("Invalid choice.");
        }
        return true;
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────

    private static int readInt() {
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
