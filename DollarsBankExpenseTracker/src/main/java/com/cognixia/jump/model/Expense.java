package com.cognixia.jump.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Expense {
	
	public static enum Category {
		HOUSING, TRANSPORTATION, FOOD, UTILITIES, CLOTHING, INSURANCE, DEFAULT
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Category category;
	
	@NotNull
	private double amount;
	
	@NotNull
	private LocalDateTime date;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	
	public Expense() {}

	public Expense(Integer id, Category category, @NotNull double amount, @NotNull LocalDateTime date, User user) {
		super();
		this.id = id;
		this.category = category;
		this.amount = amount;
		this.date = date;
		this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Expense [id=" + id + ", category=" + category + ", amount=" + amount + ", date=" + date  + "]";
	}
	
	
	
	
}
