package in.anubhavspring.journalApp.repository;

import in.anubhavspring.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

//extends MongoRepo to do the actual db calls from here

//MongoRepo will accept 2 things <Type of data, Type of id>
public interface UserRepo extends MongoRepository <User, ObjectId>{
    User findByUsername(String username);

    void deleteByUsername(String username);
}
