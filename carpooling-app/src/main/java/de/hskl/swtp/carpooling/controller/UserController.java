package de.hskl.swtp.carpooling.controller;

import de.hskl.swtp.carpooling.dto.UserDtoOut;
import de.hskl.swtp.carpooling.dto.UserLoginDTOIn;
import de.hskl.swtp.carpooling.dto.UserRegisterDTOIn;
import de.hskl.swtp.carpooling.security.SecurityManager;
import de.hskl.swtp.carpooling.model.User;
import de.hskl.swtp.carpooling.service.OfferManager;
import de.hskl.swtp.carpooling.service.RequestManager;
import de.hskl.swtp.carpooling.service.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController
{
    Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserManager userManager;
    private final SecurityManager securityManager;
    private final OfferManager offerManager;
    private final RequestManager requestManager;

    @Autowired
    public UserController(UserManager noteManger, SecurityManager securityManager, OfferManager offerManager, RequestManager requestManager)
    {
        this.userManager = noteManger;
        this.securityManager = securityManager;
        this.offerManager = offerManager;
        this.requestManager = requestManager;
    }
    //Register
    @PostMapping("/createuser")
    public ResponseEntity<UserDtoOut> createUser(@RequestBody UserRegisterDTOIn userIn )
    {
        log.info(userIn.toString());
        log.info(userIn.position().toString());
        User user=userManager.createUser( userIn );
        String accessToken = securityManager.createUserToken(user);
        return ResponseEntity.ok().header("Authorization", accessToken ).body( new UserDtoOut( user ) );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDtoOut> getUserById(@PathVariable int userId)
    {
        User user = userManager.getUserById(userId).orElse(null);
        return ResponseEntity.ok().body( new UserDtoOut( user ) );
    }




    @PostMapping("/login")
    public ResponseEntity<UserDtoOut> loginUser(@RequestBody UserLoginDTOIn userIn )
    {
        User user = userManager.loginUser( userIn.username(), userIn.password());

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
}
