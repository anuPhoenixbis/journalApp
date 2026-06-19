package in.anubhavspring.journalApp.service;

import in.anubhavspring.journalApp.entity.JournalEntry;
import in.anubhavspring.journalApp.entity.User;
import in.anubhavspring.journalApp.repository.JournalEntryRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component//so the spring stores it
@Slf4j
public class JournalEntryService {
//    this where we define certain services using the repo and to make db calls
    @Autowired
    private JournalEntryRepo journalEntryRepo;//this creates an instance of the repo interface to be used here
//    to simplify all this repo is just an instance of the MongoDBRepo
//    we are using this instance to define db calls of types as services
//    we will use these in the controller by calling these services

    @Autowired
    private UserService userService;

    @Transactional
    //so that if in any case if username is becomes null and user won't be saved
    //but the journal entry might get saved creating an inconsistency of making an entry with no owner(User)
    public void saveEntry(JournalEntry journalEntry, String username){
//        thus atomicity achieved
        try{
            User user = userService.findByUsername(username);
            JournalEntry savedEntry = journalEntryRepo.save(journalEntry);//this is the service we created to save an entry to the db
            user.getJournalEntries().add(savedEntry);//pushing the saved journal entry to the particular user
//            user.setUsername(null);//this should(to show the work of transactional) throw error and since it's a trx, we will rollback the entire thing, this is sole reason we need replica sets which is not available in local MongoDB
            userService.saveUser(user);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst saving the entry", e);
        }
    }
    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepo.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepo.findAll();
    }

//    Optional<> says that the id may be null so in that case the JournalEntry might be a null as well
//    hence, wrapped in Optional
    public Optional<JournalEntry> getById(ObjectId id){
        return journalEntryRepo.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id,String username){
        try {
            if(id==null || username == null) return false;//terminate the function if id is null
            User user = userService.findByUsername(username);
//        tho this creates a deeper problem that we are using existsById and deleteById
//        so concurrent uses may preempt in b/w causing consistency issues and race conditions

            boolean isRemoved = user.getJournalEntries().removeIf(x->x.getId().equals(id));//if we get an entry's id equal to provided id then remove it
            if(isRemoved){
                journalEntryRepo.deleteById(id);
                userService.saveUser(user);//always remember to save user Entry after making changes in the user's journalEntry list
            }
            return isRemoved;//this holds whether we were able to delete the entry or not
        } catch (Exception e) {
            log.error("Error in journal entry service deleteById", e);
            throw new RuntimeException("An error occurred whilst deleting the entry", e);
        }
    }
}
