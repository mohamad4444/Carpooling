package de.hskl.swtp.carpooling.controller;

import de.hskl.swtp.carpooling.dto.*;
import de.hskl.swtp.carpooling.model.Position;
import de.hskl.swtp.carpooling.security.SecurityManager;
import de.hskl.swtp.carpooling.model.User;
import de.hskl.swtp.carpooling.service.UserDBAccess;
import de.hskl.swtp.carpooling.util.AddressService;
import de.hskl.swtp.carpooling.util.NominatimQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController
{
    Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserDBAccess dbAccess;
    private final SecurityManager securityManager;
    private final AddressService addressService;

    @Autowired
    public UserController(UserDBAccess noteManger, SecurityManager securityManager, AddressService addressService)
    {
        this.dbAccess = noteManger;
        this.securityManager = securityManager;
        this.addressService = addressService;
    }

    //Register
    @PostMapping("/createuser")
    public ResponseEntity<UserDtoOut> createUser(@RequestBody UserRegisterDTOIn userIn )
    {
        log.info(userIn.toString());
        log.info(userIn.position().toString());
        User user= dbAccess.createUser( userIn );
        String accessToken = securityManager.createUserToken(user);
        return ResponseEntity.ok().header("Authorization", accessToken ).body( new UserDtoOut( user ) );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDtoOut> getUserById(@PathVariable int userId)
    {
        User user = dbAccess.findUserById(userId);
        return ResponseEntity.ok().body( new UserDtoOut( user ) );
    }




    @PostMapping("/login")
    public ResponseEntity<UserDtoOut> loginUser(@RequestBody UserLoginDTOIn userIn )
    {
        User user = dbAccess.loginUser( userIn.username(), userIn.password());

        String accessToken = securityManager.createUserToken(user);

        return ResponseEntity.ok().header("Authorization", accessToken ).body( new UserDtoOut( user ) );
    }

//
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
//
//    @DeleteMapping("/{userId}/offers/{noteId}")
//    public ResponseEntity<Boolean> delteNoteFromUser(@RequestHeader("Authorization") String accessToken, @PathVariable int userId, @PathVariable int noteId )
//    {
//        securityManager.checkIfTokenIsAccepted(accessToken);
//        securityManager.checkIfTokenIsFromUser(accessToken, userId);
//
////        boolean result = userManager.getUser(userId).deleteNote(noteId);
//        return ResponseEntity.ok().body( null );
//    }
@PostMapping
public ResponseEntity<PositionDtoOut> loginUser(
        @RequestBody AddressDtoIn addressDtoIn)
{
    NominatimQuery geoQuery = NominatimQuery.Builder.create()
            .street(addressDtoIn.street())
            .streetNr(addressDtoIn.streetNumber())
            .city(addressDtoIn.city())
            .postalcode(addressDtoIn.postcode()).build();
    Position position = addressService.getGeoData(geoQuery);
    PositionDtoOut positionDtoOut =
            new PositionDtoOut(String.valueOf(position.getLongitude()),
                    String.valueOf(position.getLatitude()));
    return ResponseEntity.ok().body(positionDtoOut);
}
}
