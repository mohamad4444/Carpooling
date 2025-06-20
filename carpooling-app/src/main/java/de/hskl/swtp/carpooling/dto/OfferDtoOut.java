package de.hskl.swtp.carpooling.dto;

import de.hskl.swtp.carpooling.model.Offer;
import de.hskl.swtp.carpooling.model.User;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;


public record OfferDtoOut(int offerId,String startTime,int distance) {
    private static final DateTimeFormatter instantFormatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale.GERMAN)
                    .withZone(ZoneId.systemDefault());

    public OfferDtoOut(Offer offer)
    {
        this(offer.getOfferId(),instantFormatter.format(offer.getStartTime()), offer.getDistance());
    }
}
