package com.rideshare.app.dto;

import com.rideshare.app.model.Request;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public record RequestDTOOut(
        int requestId,
        String startTimeDisplay,
        String startTimeIso
) {
    private static final DateTimeFormatter displayFormatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale.GERMAN);

    private static final DateTimeFormatter isoFormatter =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public RequestDTOOut(Request request) {
        this(
                request.getRequestId(),
                // Convert Instant to LocalDateTime in system default zone for display formatting:
                displayFormatter.format(LocalDateTime.ofInstant(request.getStartTime(), ZoneId.systemDefault())),
                // ISO format for sending back to frontend (also LocalDateTime in system default zone):
                isoFormatter.format(LocalDateTime.ofInstant(request.getStartTime(), ZoneId.systemDefault()))
        );
    }
}
