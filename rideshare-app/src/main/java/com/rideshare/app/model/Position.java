package com.rideshare.app.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Position implements Serializable {
    private double longitude;
    private double latitude;

    public Position() {}

    public Position(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
    @JsonGetter("lon")
    public double getLongitude() {
        return longitude;
    }
    @JsonSetter("lon")
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    @JsonGetter("lat")
    public double getLatitude() {
        return latitude;
    }
    @JsonSetter("lat")
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    @Override
    public String toString() {
        return "Position{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
