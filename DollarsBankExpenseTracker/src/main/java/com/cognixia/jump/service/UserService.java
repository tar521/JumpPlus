package com.cognixia.jump.service;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.cognixia.jump.exceptions.IncorrectPasswordException;
import com.cognixia.jump.exceptions.UserNotFoundException;
import com.cognixia.jump.model.Credentials;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;

@Component
public class UserService {
	
	@Autowired
	UserRepository repo;

	public User authenticate(Credentials cred) throws Exception {
		
		Optional<User> found =  repo.findByUsername(cred.getUsername());
		
		if (!found.isEmpty()) {
			String tempPass = new String(Base64.getDecoder().decode(found.get().getPassword().getBytes()));
			if (tempPass.equals(cred.getPassword())) {
				return found.get();
			}
			else {
				throw new IncorrectPasswordException();
			}
		}

		throw new UserNotFoundException();
	}
	
	public User createUser(User user) {
		user.setId(null);
		user.setPassword(Base64.getEncoder().encodeToString(user.getPassword().getBytes()));
		
		User created = repo.save(user);
		
		return created;
	}
	
	public User updateUser(User user) {
		boolean exists = repo.existsById(user.getId());
		
		if (exists) {
			User updated = repo.save(user);
			return updated;
		}
		
		return null;
		
	}
	

}
