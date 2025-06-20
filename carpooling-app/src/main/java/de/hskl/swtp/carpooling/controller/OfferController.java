package de.hskl.swtp.carpooling.controller;

import de.hskl.swtp.carpooling.dto.OfferDtoIn;
import de.hskl.swtp.carpooling.dto.OfferDtoOut;
import de.hskl.swtp.carpooling.dto.UserMapDtoOut;
import de.hskl.swtp.carpooling.model.Offer;
import de.hskl.swtp.carpooling.model.User;
import de.hskl.swtp.carpooling.service.OfferDBAccess;
import de.hskl.swtp.carpooling.security.SecurityManager;
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
}
