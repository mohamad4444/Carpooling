package de.hskl.swtp.carpooling.controller;

import de.hskl.swtp.carpooling.dto.*;
import de.hskl.swtp.carpooling.model.Offer;
import de.hskl.swtp.carpooling.model.Request;
import de.hskl.swtp.carpooling.model.User;
import de.hskl.swtp.carpooling.security.SecurityManager;
import de.hskl.swtp.carpooling.service.OfferDBAccess;
import de.hskl.swtp.carpooling.service.RequestDBAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@RestController
public class RequestController {
    private static final Logger log = LoggerFactory.getLogger(RequestController.class);
    private final RequestDBAccess dbAccess;
    private final SecurityManager securityManager;
    @Autowired
    public RequestController(RequestDBAccess dbAccess,
                             SecurityManager securityManager)
    {
        this.dbAccess = dbAccess;
        this.securityManager = securityManager;
    }
    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<RequestDTOOut> createRequest(
            @PathVariable int userId,
            @RequestHeader("Authorization") String token,
            @RequestBody RequestDTOIn requestDTOIn) {
        securityManager.checkIfTokenIsAccepted(token);
        securityManager.checkIfTokenIsFromUser(token, userId);
        Request request=dbAccess.createRequest(securityManager.getUser(token),requestDTOIn);
        RequestDTOOut requestDTOOut=new RequestDTOOut(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(requestDTOOut);
    }
    @PostMapping("/users/{userId}/requests/matches")
    public ResponseEntity<List<MatchedOfferDtoOut>> getMatchingOffers(
            @PathVariable int userId,
            @RequestHeader("Authorization") String token,
            @RequestBody RequestDTOIn requestDTOIn
    ) {
        // Log the incoming requestDTOIn startTime (assumed LocalDateTime)
        log.info("Received startTime from client (LocalDateTime): {}", requestDTOIn.startTime());

        Instant instant = requestDTOIn.startTime().atZone(ZoneId.of("Europe/Berlin")).toInstant();
        log.info("Converted to Instant (UTC): {}", instant);

        List<Offer> offers = dbAccess.findOffersWithUsersNearby(securityManager.getUser(token).getPosition(), instant);
        log.info("Found {} offers", offers.size());

        List<MatchedOfferDtoOut> offerDtos = offers.stream()
                .map(MatchedOfferDtoOut::new)
                .toList();

        return ResponseEntity.ok(offerDtos);
    }
   @GetMapping("/users/{userId}/requests")
    public ResponseEntity<List<RequestDTOOut>> getUserRequests(
            @PathVariable int userId,
            @RequestHeader("Authorization") String token
    ) {
        securityManager.checkIfTokenIsAccepted(token);
        securityManager.checkIfTokenIsFromUser(token, userId);

        List<Request> requests = dbAccess.findRequestsByUserId(userId);

        List<RequestDTOOut> result = requests.stream()
                .map(RequestDTOOut::new)
                .toList() ;

        return ResponseEntity.ok(result);
    }
    @DeleteMapping("/requests/{requestId}")
    public ResponseEntity<Void> deleteRequest(
            @PathVariable int requestId,
            @RequestHeader("Authorization") String token) {
        securityManager.checkIfTokenIsAccepted(token);

        Request request = dbAccess.getRequestById(requestId);
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found");
        }

        int userIdFromToken = securityManager.getUser(token).getUserId();
        if (request.getUser().getUserId() != userIdFromToken) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own requests");
        }

        dbAccess.deleteRequest(requestId);
        return ResponseEntity.noContent().build();
    }


}
