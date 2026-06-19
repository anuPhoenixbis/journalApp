package in.anubhavspring.journalApp.service;

import in.anubhavspring.journalApp.entity.User;
import in.anubhavspring.journalApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/*
* In spring security when we pass our username and pass, it asks us 2 questions: does the user exists?
* and if it does, what is their password and what roles does it have?
*
* UserDetailsService provides us an interface named loadUserByUsername which is used to find user from the db
* here we provide the implementation of how locate the user using its username then the auth process is handled
* by the spring security
*
    Login Request
          |
          v
    Spring Security
          |
          v
    loadUserByUsername("anubhav")
          |
          v
    MongoDB
          |
          v
    User Document
          |
          v
    UserDetails
          |
          v
    Password Verification
          |
          v
    Authenticated / Rejected
* */

@Component
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);//get the user
        if(user != null){
//            now we have the user from the user userRepo we have to convert it to details and ret it
//            maybe it's easier for security to handle it this way
            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))//we will fetch the roles from the AL we created in the User entity
                    .build();
        }
        throw new UsernameNotFoundException("User not found with username: "+ username);
    }

}
