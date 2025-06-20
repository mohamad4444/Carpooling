package de.hskl.swtp.carpooling.controller;

import de.hskl.swtp.carpooling.dto.OfferDtoIn;
import de.hskl.swtp.carpooling.dto.OfferDtoOut;
import de.hskl.swtp.carpooling.dto.RequestDTOIn;
import de.hskl.swtp.carpooling.dto.RequestDTOOut;
import de.hskl.swtp.carpooling.model.Offer;
import de.hskl.swtp.carpooling.model.Request;
import de.hskl.swtp.carpooling.security.SecurityManager;
import de.hskl.swtp.carpooling.service.OfferDBAccess;
import de.hskl.swtp.carpooling.service.RequestDBAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

@RestController
public class RequestController {
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

}
