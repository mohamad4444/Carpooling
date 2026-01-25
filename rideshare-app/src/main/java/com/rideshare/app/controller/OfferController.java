package com.rideshare.app.controller;

import com.rideshare.app.dto.OfferDtoIn;
import com.rideshare.app.dto.OfferDtoOut;
import com.rideshare.app.dto.UserMapDtoOut;
import com.rideshare.app.model.Offer;
import com.rideshare.app.model.User;
import com.rideshare.app.service.OfferDBAccess;
import com.rideshare.app.security.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

@RestController
public class OfferController {
    private final OfferDBAccess dbAccess;
    private final SecurityManager securityManager;

    @Autowired
    public OfferController(OfferDBAccess dbAccess,
                           SecurityManager securityManager) {
        this.dbAccess = dbAccess;
        this.securityManager = securityManager;
    }



    @PostMapping("/users/{userId}/offers")
    public ResponseEntity<OfferDtoOut> createOffer(
            @PathVariable int userId,
            @RequestHeader("Authorization") String token,
            @RequestBody OfferDtoIn offerDtoIn) {
        securityManager.checkIfTokenIsAccepted(token);
        securityManager.checkIfTokenIsFromUser(token, userId);
        Offer offer=dbAccess.createOffer(securityManager.getUser(token),offerDtoIn);
        OfferDtoOut offerDtoOut=new OfferDtoOut(offer);
        return ResponseEntity.status(HttpStatus.CREATED).body(offerDtoOut);
    }
    @GetMapping("/users/{userId}/offers")
    public ResponseEntity<Collection<OfferDtoOut>> getAllOffersForUser(
            @PathVariable int userId,
            @RequestHeader("Authorization") String token) {
        securityManager.checkIfTokenIsAccepted(token);
        securityManager.checkIfTokenIsFromUser(token, userId);
        Collection<Offer> offers = dbAccess.getOffersByUserId(userId);
        Collection<OfferDtoOut> dtos = offers.stream()
                .map(OfferDtoOut::new)
                .toList();
        return ResponseEntity.ok(dtos);  // HTTP 200 with users in body
    }

    //Not used atm
    @GetMapping("/users/{userId}/active-offers")
    public ResponseEntity<Collection<OfferDtoOut>> getActiveOffersForUser(
            @RequestHeader("Authorization") String token,
            @PathVariable int userId) {
        securityManager.checkIfTokenIsAccepted(token);
        securityManager.checkIfTokenIsFromUser(token, userId);
        Collection<Offer> offers = dbAccess.getPossibleOffersForUser(userId);
        List<OfferDtoOut> offerDtoOuts = offers.stream()
                .map(OfferDtoOut::new).toList();
        return ResponseEntity.ok(offerDtoOuts);
    }
    @DeleteMapping("/offers/{offerId}")
    public ResponseEntity<Void> deleteOffer(
            @PathVariable int offerId,
            @RequestHeader("Authorization") String token) {
        securityManager.checkIfTokenIsAccepted(token);

        // Verify that the offer exists and belongs to the user making the request
        Offer offer = dbAccess.getOfferById(offerId).orElse(null);
        if (offer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer not found");
        }

        int userIdFromToken = securityManager.getUser(token).getUserId();
        if (offer.getUser().getUserId() != userIdFromToken) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own offers");
        }

        dbAccess.deleteOffer(offerId);
        return ResponseEntity.noContent().build();  // HTTP 204 No Content
    }

}
