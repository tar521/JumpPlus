package com.cognixia.jump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer>{

	@Query(value = "SELECT * from contact WHERE user_id = ?1", nativeQuery = true)
	List<Contact> findByUserId(int id);

}
