package com.rideshare.app.util;

public class NominatimQuery {
    private final String street;
    private final String streetNr;
    private final String city;
    private final String postalcode;
    private final String country;

    private NominatimQuery(Builder builder) {
        this.street = builder.street;
        this.streetNr = builder.streetNr;
        this.city = builder.city;
        this.postalcode = builder.postalcode;
        this.country = builder.country;
    }

    public String getQueryString() {
        return "street=" + this.street + " " + this.streetNr +
                "&city=" + this.city +
                "&postalcode=" + this.postalcode +
                "&country=" + this.country;
    }

    public static class Builder {
        private String street;
        private String streetNr;
        private String city;
        private String postalcode;
        private String country = "Germany";

        public static Builder create() {
            return new Builder();
        }

        private Builder() {
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder streetNr(String streetNr) {
            this.streetNr = streetNr;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder postalcode(String postalcode) {
            this.postalcode = postalcode;
            return this;
        }

        public NominatimQuery build() {
            return new NominatimQuery(this);
        }
    }
}