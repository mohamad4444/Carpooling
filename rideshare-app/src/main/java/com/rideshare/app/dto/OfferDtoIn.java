package com.rideshare.app.dto;

import com.rideshare.app.model.Offer;

import java.time.Instant;
import java.time.LocalDateTime;


public record OfferDtoIn(LocalDateTime startTime, int distance) {
}
