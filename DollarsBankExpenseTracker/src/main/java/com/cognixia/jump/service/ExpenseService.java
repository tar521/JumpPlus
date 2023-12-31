package com.cognixia.jump.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognixia.jump.model.Expense;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.ExpenseRepository;

@Component
public class ExpenseService {

	@Autowired
	private ExpenseRepository repo;
	
	public Expense createExpense(Expense expense) {
		expense.setId(null);
		
		if (expense.getDate() == null) {
			expense.setDate(LocalDateTime.now());
		}
		Expense created = repo.save(expense);
		
		return created;
	}
	
	public Expense updateExpense(Expense expense) {
		boolean exists = repo.existsById(expense.getId());
		
		if (exists) {
			Expense updated = repo.save(expense);
			return updated;
		}
		
		return null;
		
	}
	
	public Expense removeExpenseById(int id) {
		Optional<Expense> found = repo.findById(Integer.valueOf(id));
		
		if (found.isEmpty()) {
			return null;
		}
		
		repo.delete(found.get());
		
		return found.get();
	}
	
	public List<Expense> getExpensesByUser(User user) {
		return repo.findExpensesByUser(user.getId());
	}
	
	public Map<Integer, Integer> expenseYears(User user) {
		HashMap<Integer, Integer> years = new HashMap<Integer, Integer>();
		for (Expense e : user.getExpenses()) {
			if (!years.containsKey(e.getDate().getYear())) {
				years.put(e.getDate().getYear(), e.getDate().getYear());
			}
		}
		
		return years;
	}

	public void sortExpenses(User user) {
		Collections.sort(user.getExpenses(), new Comparator<Expense>() {
			@Override
			public int compare(Expense e1, Expense e2) {
				return e1.getDate().compareTo(e2.getDate());
			}
		});
		
	}
}
