package com.rideshare.app.model;

import com.rideshare.app.dto.OfferDtoIn;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
@Entity
public class Offer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int offerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private  Instant startTime;
    private  int distance;

    public static Offer from(OfferDtoIn offerDtoIn) {
        Offer offer = new Offer();
        Instant instant = offerDtoIn.startTime().atZone(ZoneId.of("Europe/Berlin")).toInstant();
        offer.setStartTime(instant);
        offer.setDistance(offerDtoIn.distance());
        return offer;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public User getUser() {
        return user;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
