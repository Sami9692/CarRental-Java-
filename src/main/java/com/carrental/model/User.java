package com.carrental.model;

import java.time.LocalDate;

public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String licenseNumber;
    private boolean isAdmin;

    public User() {}

    public User(int userId, String firstName, String lastName, String email,
                LocalDate dateOfBirth, String licenseNumber, boolean isAdmin) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.licenseNumber = licenseNumber;
        this.isAdmin = isAdmin;
    }

    public int getUserId()                  { return userId; }
    public void setUserId(int v)            { this.userId = v; }
    public String getFirstName()            { return firstName; }
    public void setFirstName(String v)      { this.firstName = v; }
    public String getLastName()             { return lastName; }
    public void setLastName(String v)       { this.lastName = v; }
    public String getEmail()                { return email; }
    public void setEmail(String v)          { this.email = v; }
    public LocalDate getDateOfBirth()       { return dateOfBirth; }
    public void setDateOfBirth(LocalDate v) { this.dateOfBirth = v; }
    public String getLicenseNumber()        { return licenseNumber; }
    public void setLicenseNumber(String v)  { this.licenseNumber = v; }
    public boolean isAdmin()                { return isAdmin; }
    public void setAdmin(boolean v)         { this.isAdmin = v; }

    public String getFullName()             { return firstName + " " + lastName; }

    @Override
    public String toString() {
        return String.format("User[id=%d, name=%s %s, email=%s, admin=%b]",
                userId, firstName, lastName, email, isAdmin);
    }
}
