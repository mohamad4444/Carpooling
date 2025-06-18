package de.hskl.swtp.carpooling.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Instant startTime;

    public User getUser() {
        return user;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
