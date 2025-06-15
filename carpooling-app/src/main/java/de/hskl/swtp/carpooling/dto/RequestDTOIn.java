package de.hskl.swtp.carpooling.dto;

import java.time.Instant;

public record RequestDTOIn(
    int userId,
    Instant startTime
) {}
