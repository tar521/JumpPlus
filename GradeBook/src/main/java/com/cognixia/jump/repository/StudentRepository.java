package com.cognixia.jump.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Student;

@Repository
public interface StudentRepository extends UserRepository<Student>{

}
