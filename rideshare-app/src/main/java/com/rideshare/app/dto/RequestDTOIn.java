package com.rideshare.app.dto;

import java.time.Instant;
import java.time.LocalDateTime;

public record RequestDTOIn(
    LocalDateTime startTime
) {}
