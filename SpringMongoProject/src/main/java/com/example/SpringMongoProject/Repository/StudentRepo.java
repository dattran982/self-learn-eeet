package com.example.SpringMongoProject.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.SpringMongoProject.Model.Student;

public interface StudentRepo extends MongoRepository<Student, String> {
    // Custom query methods can be defined here if needed
    
}
