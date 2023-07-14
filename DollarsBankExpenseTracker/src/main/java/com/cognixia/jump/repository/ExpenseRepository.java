package com.cognixia.jump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

	@Query("select e from Expense e where e.user.id = ?1")
	public List<Expense> findExpensesByUser(int id);
}
