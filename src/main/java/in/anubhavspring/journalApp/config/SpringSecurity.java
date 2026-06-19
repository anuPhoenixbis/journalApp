package in.anubhavspring.journalApp.config;

import in.anubhavspring.journalApp.service.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {
    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        just how we used to filter endpoints in the next.js config, this is very similar tho a bit complexer than that
        return http.authorizeHttpRequests(req -> req
                .requestMatchers("/public/**").permitAll()//this means the public endpoint will require no auth
                .requestMatchers("/journal/**","/user/**").authenticated()//to ensure these endpoints are authenticated
                .requestMatchers("/admin/**").hasRole("ADMIN")//The user must be authenticated and have the role ADMIN
                .anyRequest().authenticated()//any other req which bypasses the above matchers should be authed
                )
//              Enable HTTP Basic authentication.This is simple and useful for testing with Postman or curl.
//              For production APIs : JWT or OAuth2 is usually preferred over Basic auth.
                .httpBasic(Customizer.withDefaults())
//              Turn off CSRF protection. This is common for stateless REST APIs that use Basic auth, JWT, or tokens.
//              For traditional server-rendered web apps with sessions, CSRF should usually remain enabled.
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

//    this where the actual auth occurs, spring security requires 2 things: how to find the user and how to verify the pass
//    this where we define both, we defined the userDetailsService interface which loads the user using its username and
//    for pass the passEncoder is how the password is encoded and this is how we verify it
//    why is it autowired? -> it can not only use to initialize a variable but also used on constructors and methods
//    to do the same thing we were doing with we were using it for so far, automatically  execute the method
//"Run this method during startup and provide the required dependencies."
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }



    @Bean
    public PasswordEncoder passwordEncoder(){
//        the password that is to be store in mongodb now gets hashed using this bean so if there occurs any kind of data leaks
//        the password remains untouched
        return new BCryptPasswordEncoder();
    }
}
