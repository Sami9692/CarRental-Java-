package com.carrental.controller;

import com.carrental.model.User;
import com.carrental.service.impl.AuthServiceImpl;

import java.util.Optional;
import java.util.Scanner;

/**
 * Controller for authentication flows (register / login / logout).
 */
public class AuthController {

    private final AuthServiceImpl authService;
    private final Scanner scanner;

    // Session state
    private User currentUser = null;

    public AuthController(Scanner scanner) {
        this.authService = new AuthServiceImpl();
        this.scanner     = scanner;
    }

    public User getCurrentUser() { return currentUser; }
    public boolean isLoggedIn()  { return currentUser != null; }
    public boolean isAdmin()     { return isLoggedIn() && currentUser.isAdmin(); }

    /** Interactive registration wizard. */
    public void register() {
        System.out.println("\n=== USER REGISTRATION ===");
        System.out.print("First Name     : "); String firstName = scanner.nextLine().trim();
        System.out.print("Last Name      : "); String lastName  = scanner.nextLine().trim();
        System.out.print("Email          : "); String email     = scanner.nextLine().trim();
        System.out.print("Date of Birth  : "); String dob       = scanner.nextLine().trim(); // yyyy-MM-dd
        System.out.print("License Number : "); String license   = scanner.nextLine().trim();
        System.out.print("Username       : "); String username  = scanner.nextLine().trim();
        System.out.print("Password       : "); String password  = scanner.nextLine().trim();

        try {
            User user = authService.register(firstName, lastName, email, dob, license, username, password);
            System.out.println("\n✔ Registration successful! Welcome, " + user.getFullName());
            currentUser = user;
        } catch (IllegalArgumentException e) {
            System.out.println("✘ Registration failed: " + e.getMessage());
        }
    }

    /** Interactive login wizard. */
    public void login() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Username : "); String username = scanner.nextLine().trim();
        System.out.print("Password : "); String password = scanner.nextLine().trim();

        Optional<User> userOpt = authService.login(username, password);
        if (userOpt.isPresent()) {
            currentUser = userOpt.get();
            System.out.println("✔ Welcome back, " + currentUser.getFullName() + "!"
                    + (currentUser.isAdmin() ? " [ADMIN]" : ""));
        } else {
            System.out.println("✘ Invalid username or password.");
        }
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("✔ Goodbye, " + currentUser.getFirstName() + "!");
            currentUser = null;
        }
    }
}
