package com.rideshare.app.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    private String streetNumber;
    private String street;
    private String zip;
    private String city;

    public Address() {}

    public Address(String streetNumber, String street, String zip, String city) {
        this.streetNumber = streetNumber;
        this.street = street;
        this.zip = zip;
        this.city = city;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
