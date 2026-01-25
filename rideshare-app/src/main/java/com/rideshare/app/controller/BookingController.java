package com.rideshare.app.controller;

import com.rideshare.app.dto.OfferDtoOut;
import com.rideshare.app.model.Offer;
import com.rideshare.app.security.SecurityManager;
import com.rideshare.app.service.OfferDBAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

@RestController
public class BookingController {
    private final OfferDBAccess dbAccess;
    private final SecurityManager securityManager;
    @Autowired
    public BookingController(OfferDBAccess dbAccess,
                             SecurityManager securityManager)
    {
        this.dbAccess = dbAccess;
        this.securityManager = securityManager;
    }
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<Collection<OfferDtoOut>> getActiveOffersForUser(
//            @RequestHeader("Authorization") String token,
//            @PathVariable int userId)
//    {
//        checkIsAccepted(token);
//        checkIsAuthorized(token,userId);
//        Collection<Offer> offers = dbAccess.getPossibleOffersForUser(userId);
//        List<OfferDtoOut> offerDtoOuts = offers.stream()
//                .map( OfferDtoOut::new).toList();
//        return ResponseEntity.ok( offerDtoOuts );
//    }
//    private void checkIsAccepted(String token)
//    {
//        if( !securityManager.isValid(token) )
//        {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
//        }
//    }
//    private void checkIsAuthorized(String token, int userId)
//    {
//        if( userId != securityManager.getUser(token).getId() )
//        {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//    }
}
