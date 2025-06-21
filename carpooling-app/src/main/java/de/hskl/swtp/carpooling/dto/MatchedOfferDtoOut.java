package de.hskl.swtp.carpooling.dto;

import de.hskl.swtp.carpooling.model.Offer;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;


public record MatchedOfferDtoOut(String startTime,String fullName,String email) {
    private static final DateTimeFormatter instantFormatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale.GERMAN)
                    .withZone(ZoneId.systemDefault());

    public MatchedOfferDtoOut(Offer offer)
    {
        this(instantFormatter.format(offer.getStartTime()),offer.getUser().getFirstname()+" "+offer.getUser().getLastname(),offer.getUser().getEmail());
    }
}
