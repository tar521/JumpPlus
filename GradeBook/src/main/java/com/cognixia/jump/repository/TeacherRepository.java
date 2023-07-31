package com.cognixia.jump.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.cognixia.jump.model.Teacher;

@Repository
public interface TeacherRepository extends UserRepository<Teacher> {
	
//	@Query(value = "SELECT * from user WHERE email = ?1", nativeQuery = true)
//	public Optional<Teacher> findByEmail(String email);

}

