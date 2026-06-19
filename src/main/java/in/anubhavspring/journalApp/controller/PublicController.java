package in.anubhavspring.journalApp.controller;

import in.anubhavspring.journalApp.entity.User;
import in.anubhavspring.journalApp.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

//    to check if the REST api works or not for our project
    @GetMapping("/health-check")
    public String healthCheck(){
        return "ok";
    }

//    so that everyone can create a user
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user){
        boolean isPassed = userService.saveNewUser(user);

        if(isPassed){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
