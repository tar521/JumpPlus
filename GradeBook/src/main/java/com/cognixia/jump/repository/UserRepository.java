package com.cognixia.jump.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.cognixia.jump.model.User;

@NoRepositoryBean
public interface UserRepository<T extends User> extends JpaRepository<T, Integer>{

	Optional<T> findByEmail(String email);
}
