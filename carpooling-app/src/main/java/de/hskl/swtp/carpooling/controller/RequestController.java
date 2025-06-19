package de.hskl.swtp.carpooling.controller;

import de.hskl.swtp.carpooling.dto.OfferDtoOut;
import de.hskl.swtp.carpooling.model.Offer;
import de.hskl.swtp.carpooling.security.SecurityManager;
import de.hskl.swtp.carpooling.service.OfferDBAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

@RestController
public class RequestController {
    private final OfferDBAccess dbAccess;
    private final SecurityManager securityManager;
    @Autowired
    public RequestController(OfferDBAccess dbAccess,
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
//        //checkIsAccepted(token);
//        checkIsAuthorized(token,userId);
//        Collection<Offer> offers = dbAccess.getPossibleOffersForUser(userId);
//        List<OfferDtoOut> offerDtoOuts = offers.stream()
//                .map( OfferDtoOut::new).toList();
//        return ResponseEntity.ok( offerDtoOuts );
//    }
//    @GetMapping("/{userId}/requests")
//    public ResponseEntity<List<NoteDtoOut>> getNotesFromUser(@RequestHeader("Authorization") String accessToken, @PathVariable int userId)
//    {
//        securityManager.checkIfTokenIsAccepted(accessToken);
//        securityManager.checkIfTokenIsFromUser(accessToken, userId);
//
////        List<Note> notes = userManager.getUserById(userId).orElse(null).getp
////        List<NoteDtoOut> noteDtos = notes.stream().map(NoteDtoOut::new).toList();
//        return ResponseEntity.ok().body(null);
//    }
//    @GetMapping("/{userId}/requests")
//    public ResponseEntity<List<NoteDtoOut>> getNotesFromUser(@RequestHeader("Authorization") String accessToken, @PathVariable int userId)
//    {
//        securityManager.checkIfTokenIsAccepted(accessToken);
//        securityManager.checkIfTokenIsFromUser(accessToken, userId);
//
////        List<Note> notes = userManager.getUserById(userId).orElse(null).getp
////        List<NoteDtoOut> noteDtos = notes.stream().map(NoteDtoOut::new).toList();
//        return ResponseEntity.ok().body(null);
//    }
//
//    @PostMapping("/{userId}/request")
//    public ResponseEntity<NoteDtoOut> addNoteForUser(@RequestHeader("Authorization") String accessToken, @PathVariable int userId, @RequestBody NoteDtoIn noteIn )
//    {
//        securityManager.checkIfTokenIsAccepted(accessToken);
//        securityManager.checkIfTokenIsFromUser(accessToken, userId);
//
////        Note note = userManager.getUserById(userId).addNote( noteIn.content() );
//        return ResponseEntity.ok().body( null );
//    }
//    private void checkIsAccepted(String token)
//    {
//        if( !securityManager.isValid(token) )
//        {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
//        }
//    }
    private void checkIsAuthorized(String token, int userId)
    {
        if( userId != securityManager.getUser(token).getId() )
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
