package com.amigoscode.demo.repository;

import com.amigoscode.demo.collections.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface  StudentRepository extends
        MongoRepository<Student, String> {

    Optional<Student> findStudentByEmail(String email);
}
