package com.carrental.service.impl;

import com.carrental.dao.UserDAO;
import com.carrental.dao.impl.UserDAOImpl;
import com.carrental.model.User;
import com.carrental.model.UserCredential;
import com.carrental.util.PasswordUtil;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Handles user registration and login.
 */
public class AuthServiceImpl {

    private final UserDAO userDAO;

    public AuthServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Registers a new user. Throws IllegalArgumentException if the email or username is already taken.
     */
    public User register(String firstName, String lastName, String email,
                         String dateOfBirthStr, String licenseNumber,
                         String username, String plainPassword) {

        // Validate email uniqueness
        if (userDAO.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }

        // Validate username uniqueness
        if (userDAO.findCredentialByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already taken: " + username);
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setDateOfBirth(LocalDate.parse(dateOfBirthStr));
        user.setLicenseNumber(licenseNumber);
        user.setAdmin(false);

        UserCredential credential = new UserCredential();
        credential.setUsername(username);
        credential.setPasswordHash(PasswordUtil.createPasswordHash(plainPassword));

        userDAO.registerUser(user, credential);
        return user;
    }

    /**
     * Authenticates a user. Returns the User on success, or empty on failure.
     */
    public Optional<User> login(String username, String plainPassword) {
        Optional<UserCredential> credOpt = userDAO.findCredentialByUsername(username);
        if (credOpt.isEmpty()) return Optional.empty();

        UserCredential cred = credOpt.get();
        if (!PasswordUtil.verify(plainPassword, cred.getPasswordHash())) return Optional.empty();

        return userDAO.findById(cred.getUserId());
    }
}
