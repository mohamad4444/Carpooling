package de.hskl.swtp.carpooling.model;

import jakarta.persistence.*;

import java.time.Instant;
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
