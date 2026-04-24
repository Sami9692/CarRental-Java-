package com.carrental.model;

import java.time.LocalDate;

/** Junction entity representing an active or completed rental. */
public class UserRentsCar {
    private int rentalId;
    private int userId;
    private int carId;
    private int reservationId;
    private LocalDate actualPickupDate;
    private LocalDate actualReturnDate;
    private String rentalStatus;    // "active" | "completed"

    public UserRentsCar() {}

    public UserRentsCar(int rentalId, int userId, int carId, int reservationId,
                        LocalDate actualPickupDate, LocalDate actualReturnDate, String rentalStatus) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.carId = carId;
        this.reservationId = reservationId;
        this.actualPickupDate = actualPickupDate;
        this.actualReturnDate = actualReturnDate;
        this.rentalStatus = rentalStatus;
    }

    public int getRentalId()                        { return rentalId; }
    public void setRentalId(int v)                  { this.rentalId = v; }
    public int getUserId()                          { return userId; }
    public void setUserId(int v)                    { this.userId = v; }
    public int getCarId()                           { return carId; }
    public void setCarId(int v)                     { this.carId = v; }
    public int getReservationId()                   { return reservationId; }
    public void setReservationId(int v)             { this.reservationId = v; }
    public LocalDate getActualPickupDate()          { return actualPickupDate; }
    public void setActualPickupDate(LocalDate v)    { this.actualPickupDate = v; }
    public LocalDate getActualReturnDate()          { return actualReturnDate; }
    public void setActualReturnDate(LocalDate v)    { this.actualReturnDate = v; }
    public String getRentalStatus()                 { return rentalStatus; }
    public void setRentalStatus(String v)           { this.rentalStatus = v; }

    @Override
    public String toString() {
        return String.format("UserRentsCar[rentalId=%d, userId=%d, carId=%d, status=%s]",
                rentalId, userId, carId, rentalStatus);
    }
}
