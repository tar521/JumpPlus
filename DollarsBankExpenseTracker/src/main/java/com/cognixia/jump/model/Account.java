package com.cognixia.jump.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	private LocalDateTime dateCreated;
	
	@OneToMany(mappedBy = "account")
	@JsonIgnore
	private List<Expense> expenses;
	
	@OneToOne(mappedBy = "account")
	private User user;
	
	public Account(Integer id, @NotNull LocalDateTime dateCreated, List<Expense> expenses, User user) {
		super();
		this.id = id;
		this.dateCreated = dateCreated;
		this.expenses = expenses;
		this.user = user;
	}

	public Account() {}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}
	
	

}
