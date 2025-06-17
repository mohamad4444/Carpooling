package de.hskl.swtp.carpooling.controller;

import de.hskl.swtp.carpooling.dto.UserDtoOut;
import de.hskl.swtp.carpooling.dto.UserLoginDTOIn;
import de.hskl.swtp.carpooling.dto.UserRegisterDTOIn;
import de.hskl.swtp.carpooling.security.SecurityManager;
import de.hskl.swtp.carpooling.model.User;
import de.hskl.swtp.carpooling.service.DBAccess;
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
    private final DBAccess dbAccess;
    private final SecurityManager securityManager;

    @Autowired
    public UserController(DBAccess noteManger, SecurityManager securityManager)
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
        User user= dbAccess.createUser( userIn );
        String accessToken = securityManager.createUserToken(user);
        return ResponseEntity.ok().header("Authorization", accessToken ).body( new UserDtoOut( user ) );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDtoOut> getUserById(@PathVariable int userId)
    {
        User user = dbAccess.getUserById(userId).orElse(null);
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
}
