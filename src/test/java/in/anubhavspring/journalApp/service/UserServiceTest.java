package in.anubhavspring.journalApp.service;

import in.anubhavspring.journalApp.entity.User;
import in.anubhavspring.journalApp.repository.UserRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//to run a specific class so that the userRepo here which we autowired starts running so that we can use it
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepo userRepo;

//    @Disabled//to disable a test
    @ParameterizedTest
    @CsvSource({
            "AnubhavBiswas",
            "AnubhavB"
    })
    public void testByUsername(String username){
        User user = userRepo.findByUsername(username);
//        assertNotNull(userRepo.findByUsername(username));
        assertTrue(user.getJournalEntries().isEmpty(),"Journal Entries not empty for "+username);
    }

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,8,10",
            "345,23,5552"
    })
    public void test(int a, int b, int expected){
        assertEquals(expected,a+b);
    }
}
