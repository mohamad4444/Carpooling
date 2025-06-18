package de.hskl.swtp.carpooling.controller;

import de.hskl.swtp.carpooling.dto.OfferDtoOut;
import de.hskl.swtp.carpooling.model.Offer;
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
@RequestMapping("/offers")
public class OfferController {
    private final OfferDBAccess dbAccess;
    private final SecurityManager securityManager;
    @Autowired
    public OfferController(OfferDBAccess dbAccess,
                           SecurityManager securityManager)
    {
        this.dbAccess = dbAccess;
        this.securityManager = securityManager;
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<Collection<OfferDtoOut>> getActiveOffersForUser(
            @RequestHeader("Authorization") String token,
            @PathVariable int userId)
    {
        checkIsAccepted(token);
        checkIsAuthorized(token,userId);
        Collection<Offer> offers = dbAccess.getPossibleOffersForUser(userId);
        List<OfferDtoOut> offerDtoOuts = offers.stream()
                .map( OfferDtoOut::new).toList();
        return ResponseEntity.ok( offerDtoOuts );
    }
    private void checkIsAccepted(String token)
    {
        if( !securityManager.isValid(token) )
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
    private void checkIsAuthorized(String token, int userId)
    {
        if( userId != securityManager.getUser(token).getId() )
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
