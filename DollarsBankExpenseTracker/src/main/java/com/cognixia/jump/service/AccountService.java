package com.cognixia.jump.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognixia.jump.model.Account;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.AccountRespository;

@Component
public class AccountService {
	
	@Autowired
	private AccountRespository repo;
	
	public Account getAccount(User user) {
		Optional<Account> found = repo.findById(user.getAccount().getId());
		
		if (found.isEmpty()) {
			return null;
		}
		return found.get();
	}
	
	public Account createAccount(Account account) {
		account.setId(null);
		account.setDateCreated(LocalDateTime.now());
		
		Account created = repo.save(account);
		
		return created;
	}
	
	public Account updateAccount(Account account) {
		boolean exists = repo.existsById(account.getId());
		
		if (exists) {
			Account updated = repo.save(account);
			return updated;
		}
		
		return null;
		
	}

}
