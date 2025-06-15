package de.hskl.swtp.carpooling.dto;

import java.time.Instant;

public record RequestDTOOut(
    int requestId,
    int userId,
    Instant startTime
) {}
