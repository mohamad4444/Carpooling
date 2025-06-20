package de.hskl.swtp.carpooling.dto;

import de.hskl.swtp.carpooling.model.Offer;

import java.time.Instant;
import java.time.LocalDateTime;


public record OfferDtoIn(LocalDateTime startTime, int distance) {
}
