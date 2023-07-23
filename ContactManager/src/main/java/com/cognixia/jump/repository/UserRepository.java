package com.cognixia.jump.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	@Query(value = "SELECT * from user WHERE email = ?1", nativeQuery = true)
	public Optional<User> findByEmail(String email);
}
