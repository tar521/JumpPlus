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
	private UserRepository repo;

	public void createUser(User newUser) {
		newUser.setId(null);
		newUser.setPassword(new String(Base64.getEncoder().encodeToString(newUser.getPassword().getBytes())));
		
		repo.save(newUser);
	}

	public User authenticate(Credentials login) throws IncorrectPasswordException, UserNotFoundException {
		
		Optional<User> found =  repo.findByEmail(login.getEmail());
		
		if (!found.isEmpty()) {
			String tempPass = new String(Base64.getDecoder().decode(found.get().getPassword().getBytes()));
			if (tempPass.equals(login.getPassword())) {
				return found.get();
			}
			else {
				throw new IncorrectPasswordException();
			}
		}

		throw new UserNotFoundException();
		
	}

}
