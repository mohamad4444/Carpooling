package de.hskl.swtp.carpooling.dto;

import de.hskl.swtp.carpooling.model.Offer;
import de.hskl.swtp.carpooling.model.Request;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public record RequestDTOOut(
    int requestId,
    String startTime
) {
    private static final DateTimeFormatter instantFormatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale.GERMAN)
                    .withZone(ZoneId.systemDefault());
    public RequestDTOOut(Request request) {
        this(request.getRequestId(),instantFormatter.format(request.getStartTime()));
    }
}
