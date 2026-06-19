package in.anubhavspring.journalApp.service;

import in.anubhavspring.journalApp.entity.User;
import in.anubhavspring.journalApp.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@SpringBootTest
public class UserDetailsServiceImplementationTest {

    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

//    say I want to use a db call to test out something, but we can't always have that to test
//    instead, we use mockito to mock the resources and use the contents received for test

//    Mocks create fake obj outside spring to be used to mock certain results, but since doesn't know that
//    the Mock exists it doesn't inject it. But here MockitoBean spring creates an application context
//    which then replace the UserRepo Bean here(mocks the repo) and injects the mocked UserRepo into the UserServiceDetails

//    Mock isn't seen by spring it works independently by MockitoBean works with spring application context
//    and replaces the real Bean
    @MockitoBean
    private UserRepo userRepo;

    @Test
    void loadUserByUsername() {
        when(userRepo.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(
                        User.builder()
                                .username("AnubhavB")
                                .password("ferefrergg")
                                .roles(new ArrayList<>())
                                .build()
                );
        UserDetails user = userDetailsService.loadUserByUsername("AnubhavB");
        Assertions.assertNotNull(user);
    }
}
