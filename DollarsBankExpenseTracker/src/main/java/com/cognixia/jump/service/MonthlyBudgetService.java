package com.cognixia.jump.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognixia.jump.model.MonthlyBudget;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.MonthlyBudgetRepository;

@Component
public class MonthlyBudgetService {
	
	@Autowired
	private MonthlyBudgetRepository repo;
	
	public MonthlyBudget createExpense(MonthlyBudget budget) {
		budget.setId(null);
		
		MonthlyBudget created = repo.save(budget);
		
		return created;
	}
	
	public MonthlyBudget updateExpense(MonthlyBudget budget) {
		boolean exists = repo.existsById(budget.getId());
		
		if (exists) {
			MonthlyBudget updated = repo.save(budget);
			return updated;
		}
		
		return null;
		
	}
	
	public List<MonthlyBudget> getBudgetByUser(User user) {
		return repo.findBudgetByUserId(user.getId());
	}

	public boolean yearExists(String year, User user) {
		for (MonthlyBudget b : user.getBudget()) {
			if (b.getYear().equals(year)) {
				return true;
			}
		}
		return false;
	}

	

}
