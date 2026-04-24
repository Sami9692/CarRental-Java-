package com.carrental.model;

import java.time.LocalDate;

public class Reservation {
    private int reservationId;
    private int userId;
    private int carId;
    private int locationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;          // "pending" | "confirmed" | "cancelled" | "completed"
    private int insuranceTypeId;

    public Reservation() {}

    public Reservation(int reservationId, int userId, int carId, int locationId,
                       LocalDate startDate, LocalDate endDate,
                       String status, int insuranceTypeId) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.carId = carId;
        this.locationId = locationId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.insuranceTypeId = insuranceTypeId;
    }

    public int getReservationId()               { return reservationId; }
    public void setReservationId(int v)         { this.reservationId = v; }
    public int getUserId()                      { return userId; }
    public void setUserId(int v)                { this.userId = v; }
    public int getCarId()                       { return carId; }
    public void setCarId(int v)                 { this.carId = v; }
    public int getLocationId()                  { return locationId; }
    public void setLocationId(int v)            { this.locationId = v; }
    public LocalDate getStartDate()             { return startDate; }
    public void setStartDate(LocalDate v)       { this.startDate = v; }
    public LocalDate getEndDate()               { return endDate; }
    public void setEndDate(LocalDate v)         { this.endDate = v; }
    public String getStatus()                   { return status; }
    public void setStatus(String v)             { this.status = v; }
    public int getInsuranceTypeId()             { return insuranceTypeId; }
    public void setInsuranceTypeId(int v)       { this.insuranceTypeId = v; }

    public long getDurationDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }

    @Override
    public String toString() {
        return String.format("Reservation[id=%d, carId=%d, userId=%d, %s to %s, status=%s]",
                reservationId, carId, userId, startDate, endDate, status);
    }
}
