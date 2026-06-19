package in.anubhavspring.journalApp.controller;

import in.anubhavspring.journalApp.entity.JournalEntry;
import in.anubhavspring.journalApp.entity.User;
import in.anubhavspring.journalApp.service.JournalEntryService;
import in.anubhavspring.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


//controller --calls--> services --calls--> repository

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired//automatically fetches the instance of service class
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesByUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();//get the username from the auth context

        User user = userService.findByUsername(username);
        List<JournalEntry> journals = user.getJournalEntries();
        if(journals != null && !journals.isEmpty()){
            return new ResponseEntity<>(journals,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntryForUser(@RequestBody JournalEntry journalEntry){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();//get the username from the auth context
        try {
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(journalEntry,username);
            return new ResponseEntity<>(journalEntry,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();//get the username from the auth context

        User user = userService.findByUsername(username);

//        doing this so that some other journal's id won't be hampered by the current user
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(entry-> entry.getId().equals(id)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.getById(id);
    //        similarly we will pass optional here as well
    //        return journalEntryService.getById(id).orElse(null);//if a null entry then return null
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    ResponseEntity is used to send responses based to Postman
    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();//get the username from the auth context

        boolean del = journalEntryService.deleteById(id,username);

        if(del) return ResponseEntity.ok("Entry deleted");

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);//we don't find the entry to be deleted
    }

    @PutMapping("id/{id}")
    public ResponseEntity<JournalEntry> updateEntryById(
            @PathVariable ObjectId id,
            @RequestBody JournalEntry newEntry
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();//get the username from the auth context

//        only if I have the journal then can I update it
        User user = userService.findByUsername(username);

        List<JournalEntry> collect = user.getJournalEntries().stream().filter(e->e.getId().equals(id)).collect(Collectors.toList());

        if(!collect.isEmpty()){
            Optional<JournalEntry> entry = journalEntryService.getById(id);
            if(entry.isPresent()){
                JournalEntry e = entry.get();
                e.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : e.getTitle());
                e.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : e.getContent());
                journalEntryService.saveEntry(e);
                return new ResponseEntity<>(e,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
