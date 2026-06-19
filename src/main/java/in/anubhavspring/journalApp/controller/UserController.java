package in.anubhavspring.journalApp.controller;

import in.anubhavspring.journalApp.entity.JournalEntry;
import in.anubhavspring.journalApp.entity.User;
import in.anubhavspring.journalApp.repository.UserRepo;
import in.anubhavspring.journalApp.service.JournalEntryService;
import in.anubhavspring.journalApp.service.UserService;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


//controller --calls--> services --calls--> repository

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAll();
    }

//    since everyone should be able to create a user we added it to public controller

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
//        we will no more pass the username and pass as the path var
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();//fetch the body pass to the basic Auth
        String username = auth.getName();//fetch the username

        User currUser = userService.findByUsername(username);
        if(currUser != null){
            currUser.setUsername(user.getUsername());
            currUser.setPassword(user.getPassword());
            boolean isPassed = userService.saveNewUser(currUser);
            if(isPassed){
                return new  ResponseEntity<>(HttpStatus.OK);
            }else return new   ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestBody User user){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        userRepo.deleteByUsername(username);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
