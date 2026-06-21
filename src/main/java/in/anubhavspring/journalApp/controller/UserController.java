package in.anubhavspring.journalApp.controller;

import in.anubhavspring.journalApp.api.response.WeatherResponse;
import in.anubhavspring.journalApp.entity.JournalEntry;
import in.anubhavspring.journalApp.entity.User;
import in.anubhavspring.journalApp.repository.UserRepo;
import in.anubhavspring.journalApp.service.JournalEntryService;
import in.anubhavspring.journalApp.service.UserService;
import in.anubhavspring.journalApp.service.WeatherService;
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

    @Autowired
    private WeatherService weatherService;

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
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

    /**
     * Deletes the authenticated user.
     *
     * @return a ResponseEntity with HTTP status 204 (NO_CONTENT)
     */
    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestBody User user){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        userRepo.deleteByUsername(username);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * Returns a personalized greeting for the authenticated user with weather information from Jamshedpur.
     *
     * @return a response containing "Hola" followed by the username and, if weather data is available, the feels-like temperature at Jamshedpur
     */
    @GetMapping("/greet")
    public ResponseEntity<?> greeting(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Jamshedpur");
        String greet="";
        if(weatherResponse != null){
             greet = " Feels like " +weatherResponse.getCurrent().getFeelslike()+" at Jamshedpur";
        }

        return new ResponseEntity<>("Hola "+auth.getName() + greet,HttpStatus.OK);
    }
}
