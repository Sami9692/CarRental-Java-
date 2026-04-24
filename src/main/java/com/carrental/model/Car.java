package com.carrental.model;

import java.math.BigDecimal;

public class Car {
    private int carId;
    private String make;
    private String model;
    private int year;
    private String licensePlate;
    private String status;          // "available" | "rented" | "maintenance"
    private BigDecimal dailyRate;
    private int typeId;
    private int locationId;
    private String imageUrl;

    public Car() {}

    public Car(int carId, String make, String model, int year,
               String licensePlate, String status, BigDecimal dailyRate,
               int typeId, int locationId, String imageUrl) {
        this.carId = carId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.status = status;
        this.dailyRate = dailyRate;
        this.typeId = typeId;
        this.locationId = locationId;
        this.imageUrl = imageUrl;
    }

    // Getters & Setters
    public int getCarId()                   { return carId; }
    public void setCarId(int v)             { this.carId = v; }
    public String getMake()                 { return make; }
    public void setMake(String v)           { this.make = v; }
    public String getModel()                { return model; }
    public void setModel(String v)          { this.model = v; }
    public int getYear()                    { return year; }
    public void setYear(int v)              { this.year = v; }
    public String getLicensePlate()         { return licensePlate; }
    public void setLicensePlate(String v)   { this.licensePlate = v; }
    public String getStatus()               { return status; }
    public void setStatus(String v)         { this.status = v; }
    public BigDecimal getDailyRate()        { return dailyRate; }
    public void setDailyRate(BigDecimal v)  { this.dailyRate = v; }
    public int getTypeId()                  { return typeId; }
    public void setTypeId(int v)            { this.typeId = v; }
    public int getLocationId()              { return locationId; }
    public void setLocationId(int v)        { this.locationId = v; }
    public String getImageUrl()             { return imageUrl; }
    public void setImageUrl(String v)       { this.imageUrl = v; }

    @Override
    public String toString() {
        return String.format("Car[id=%d, %d %s %s, plate=%s, rate=$%.2f/day, status=%s]",
                carId, year, make, model, licensePlate, dailyRate, status);
    }
}
