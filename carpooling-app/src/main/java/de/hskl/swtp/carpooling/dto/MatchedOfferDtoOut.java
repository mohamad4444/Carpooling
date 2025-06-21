package de.hskl.swtp.carpooling.dto;

import de.hskl.swtp.carpooling.model.Offer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public record MatchedOfferDtoOut(String startTimeDisplay, Instant startTimeUtc, String fullName, String email) {
    private static final DateTimeFormatter germanFormatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale.GERMAN)
                    .withZone(ZoneId.of("Europe/Berlin"));

    public MatchedOfferDtoOut(Offer offer) {
        this(
                germanFormatter.format(offer.getStartTime()), // formatted for display
                offer.getStartTime(),                         // raw UTC Instant
                offer.getUser().getFirstname() + " " + offer.getUser().getLastname(),
                offer.getUser().getEmail()
        );
    }
}
