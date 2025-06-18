package de.hskl.swtp.carpooling.controller;

import de.hskl.swtp.carpooling.dto.*;
import de.hskl.swtp.carpooling.model.Position;
import de.hskl.swtp.carpooling.security.SecurityManager;
import de.hskl.swtp.carpooling.model.User;
import de.hskl.swtp.carpooling.service.UserDBAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController
{
    //System.out.println( alternative)
    Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserDBAccess dbAccess;
    private final SecurityManager securityManager;

    @Autowired
    public UserController(UserDBAccess noteManger, SecurityManager securityManager)
    {
        this.dbAccess = noteManger;
        this.securityManager = securityManager;
    }

    //Register
    @PostMapping("/createuser")
    public ResponseEntity<UserDtoOut> createUser(@RequestBody UserRegisterDTOIn userIn )
    {
        log.info(userIn.toString());
        log.info(userIn.position().toString());
        System.out.println(userIn);
        User user= dbAccess.createUser( userIn );
        String accessToken = securityManager.createUserToken(user);
        return ResponseEntity.ok().header("Authorization", accessToken ).body( new UserDtoOut( user ) );
    }

}
