package com.carrental.model;

public class RentalLocation {
    private int locationId;
    private String city;
    private String state;
    private String country;
    private String address;

    public RentalLocation() {}

    public RentalLocation(int locationId, String city, String state, String country, String address) {
        this.locationId = locationId;
        this.city = city;
        this.state = state;
        this.country = country;
        this.address = address;
    }

    public int getLocationId()            { return locationId; }
    public void setLocationId(int v)      { this.locationId = v; }
    public String getCity()               { return city; }
    public void setCity(String v)         { this.city = v; }
    public String getState()              { return state; }
    public void setState(String v)        { this.state = v; }
    public String getCountry()            { return country; }
    public void setCountry(String v)      { this.country = v; }
    public String getAddress()            { return address; }
    public void setAddress(String v)      { this.address = v; }

    @Override
    public String toString() {
        return String.format("Location[id=%d, %s, %s, %s]", locationId, address, city, country);
    }
}
