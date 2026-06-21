package in.anubhavspring.journalApp.service;

import in.anubhavspring.journalApp.entity.User;
import in.anubhavspring.journalApp.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepo userRepo;

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();//we will use this to decode the pass from the given hash

    //    to enable the logger by slf4j, final and static we won't instantiate multiple loggers and use it anywhere in the class
    //    passed the class whose logs we wanted, instead sout everywhere we can use this ,this provides us with tons of features
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public boolean saveNewUser(User user){//for new users
        try{
            user.setPassword(encoder.encode(user.getPassword()));//we are encoding the pass twice that's why it was showing bad credentials
            user.setRoles(Arrays.asList("USER"));
            userRepo.save(user);
            return true;
        }catch(Exception e){
//            logger.error(e.getMessage());
            log.error("Error occurred for {} : {}",user.getUsername(),e.getMessage());
            return false;
        }
    }
    public void saveAdmin(User user){//for new users
        user.setPassword(encoder.encode(user.getPassword()));//we are encoding the pass twice that's why it was showing bad credentials
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepo.save(user);
    }
    public void saveUser(User user){//for previously present users (to update / journal updates)
        userRepo.save(user);//just to save the user
    }

    public List<User> getAll(){
        return userRepo.findAll();
    }

    public Optional<User> findBYId(ObjectId id){
        return userRepo.findById(id);
    }

    public void deleteById(ObjectId id){
        userRepo.deleteById(id);
    }

    public User findByUsername(String username){
        return userRepo.findByUsername(username);
    }
}
