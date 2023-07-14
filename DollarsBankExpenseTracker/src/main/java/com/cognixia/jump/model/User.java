package com.cognixia.jump.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.cognixia.jump.util.ColorUtility;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank
	@Column(unique = true, nullable = false)
	private String username;
	
	@NotBlank
	@Column(nullable = false)
	private String password;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String address;
	
	@NotBlank
	private String phone;
	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<Expense> expenses;
	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<MonthlyBudget> budget;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id")
	private Account account;
	
	public User(Integer id, @NotBlank String username, @NotBlank String password, @NotBlank String name,
			@NotBlank String address, @NotBlank String phone, List<Expense> expenses, List<MonthlyBudget> budget,
			Account account) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.expenses = expenses;
		this.budget = budget;
		this.account = account;
	}

	public User() {
		
	}


	public List<MonthlyBudget> getBudget() {
		return budget;
	}

	public void setBudget(List<MonthlyBudget> budget) {
		this.budget = budget;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}

	@Override
	public String toString() {
		return "\n" + ColorUtility.PURPLE_TEXT + "CONTACT INFO:" + ColorUtility.TEXT_RESET
				+ "\nName:        \t" + name
				+ "\nAddress:     \t" + address
				+ "\nPhone #:     \t" + phone
				
				+ "\n\n" + ColorUtility.PURPLE_TEXT + "ACCOUNT INFO:" + ColorUtility.TEXT_RESET
				+ "\nCustomer ID: \t" + id.toString()
				+ "\nUsername:    \t" + username
				+ "\nPassword:    \t" + password
				+ "\n";
	}

	
}
