package de.hskl.swtp.carpooling.dto;

import de.hskl.swtp.carpooling.model.Offer;
import de.hskl.swtp.carpooling.model.User;

public record OfferDtoOut(int offerId) {
    public OfferDtoOut(Offer offer)
    {
        this( offer.getDistance());
    }

}
