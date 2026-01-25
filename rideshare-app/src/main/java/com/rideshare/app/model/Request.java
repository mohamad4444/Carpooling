package com.rideshare.app.model;

import com.rideshare.app.dto.RequestDTOIn;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.ZoneId;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Instant startTime;

    public static Request from(RequestDTOIn requestDTOIn) {
        Request request = new Request();
        Instant instant = requestDTOIn.startTime().atZone(ZoneId.of("Europe/Berlin")).toInstant();
        request.setStartTime(instant);
        return request;
    }

    public User getUser() {
        return user;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}
