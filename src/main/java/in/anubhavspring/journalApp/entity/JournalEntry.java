package in.anubhavspring.journalApp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

//the document annotation changes this to an entry type for the mongodb
//this document annotation comes from the ORM to make it an actual document
//these annotation are provided by the ORM to help us convert a regular class to a table entity
@Document(collection = "journal_entries")//naming our collection or default will use the class name
@Data
@NoArgsConstructor
public class JournalEntry {
    @Id //makes this as Id of the row
    private ObjectId id;

    private LocalDateTime date;

    @NonNull
    private String title;
    private String content;
}

//lombok helps us replace boilerplate code with annotations like it remove getters,
//setters, constructors, and more
