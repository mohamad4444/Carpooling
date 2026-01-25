package com.rideshare.app.controller;

import com.rideshare.app.dto.*;
import com.rideshare.app.model.Position;
import com.rideshare.app.security.SecurityManager;
import com.rideshare.app.model.User;
import com.rideshare.app.util.NominatimRESTService;
import com.rideshare.app.service.UserDBAccess;
import com.rideshare.app.util.AddressService;
import com.rideshare.app.util.NominatimQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UserController {
    Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserDBAccess dbAccess;
    private final SecurityManager securityManager;

    @Autowired
    public UserController(UserDBAccess noteManger, SecurityManager securityManager) {
        this.dbAccess = noteManger;
        this.securityManager = securityManager;
    }

    //Register
    @PostMapping("/createuser")
    public ResponseEntity<UserDtoOut> createUser(@RequestBody UserRegisterDTOIn userIn) {
        log.info(userIn.toString());
        User user = dbAccess.createUser(userIn);
        log.info(user.getPosition().toString());
        String accessToken = securityManager.createUserToken(user);
        log.info("Access token: " + accessToken);
        return ResponseEntity.ok().header("Authorization", accessToken).body(new UserDtoOut(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDtoOut> loginUser(@RequestBody UserLoginDTOIn userIn) {
        User user = dbAccess.loginUser(userIn.username(), userIn.password());

        String accessToken = securityManager.createUserToken(user);

        return ResponseEntity.ok().header("Authorization", accessToken).body(new UserDtoOut(user));
    }

    @DeleteMapping("/logout/{userId}")
    public ResponseEntity<Void> logoutUser(@PathVariable int userId,
                                           @RequestHeader("Authorization") String accessToken) {
        log.info("Logout: userid={},accestoken={}", userId, accessToken);
        securityManager.checkIfTokenIsAccepted(accessToken);
        securityManager.checkIfTokenIsFromUser(accessToken, userId);
        securityManager.removeToken(accessToken);
        return ResponseEntity.noContent().build();  // HTTP 204
    }

    @GetMapping
    public ResponseEntity<Collection<UserMapDtoOut>> getAllUsers() {
        Collection<User> users = dbAccess.getAllUsers();
        Collection<UserMapDtoOut> dtos = users.stream()
                .map(UserMapDtoOut::new)
                .toList();
        return ResponseEntity.ok(dtos);  // HTTP 200 with users in body
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId
    , @RequestHeader("Authorization") String accessToken) {
        securityManager.checkIfTokenIsAccepted(accessToken);
        securityManager.checkIfTokenIsFromUser(accessToken, userId);
        securityManager.removeToken(accessToken);
        dbAccess.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
