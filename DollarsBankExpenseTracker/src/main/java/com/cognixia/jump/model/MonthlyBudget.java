package com.cognixia.jump.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class MonthlyBudget {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	private String year;
	
	@NotNull
	private Double january;
	
	@NotNull
	private Double february;
	
	@NotNull
	private Double march;
	
	@NotNull
	private Double april;
	
	@NotNull
	private Double may;
	
	@NotNull
	private Double june;
	
	@NotNull
	private Double july;
	
	@NotNull
	private Double august;
	
	@NotNull
	private Double september;
	
	@NotNull
	private Double october;
	
	@NotNull
	private Double november;
	
	@NotNull
	private Double december;
	
	@NotNull
	private Double yearlyBudget;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	
	public MonthlyBudget(Integer id, @NotNull String year, @NotNull Double january, @NotNull Double february,
			@NotNull Double march, @NotNull Double april, @NotNull Double may, @NotNull Double june,
			@NotNull Double july, @NotNull Double august, @NotNull Double september, @NotNull Double october,
			@NotNull Double november, @NotNull Double december, @NotNull Double yearlyBudget, User user) {
		super();
		this.id = id;
		this.year = year;
		this.january = january;
		this.february = february;
		this.march = march;
		this.april = april;
		this.may = may;
		this.june = june;
		this.july = july;
		this.august = august;
		this.september = september;
		this.october = october;
		this.november = november;
		this.december = december;
		this.yearlyBudget = yearlyBudget;
		this.user = user;
	}

	public MonthlyBudget() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Double getJanuary() {
		return january;
	}

	public void setJanuary(Double january) {
		this.january = january;
	}

	public Double getFebruary() {
		return february;
	}

	public void setFebruary(Double february) {
		this.february = february;
	}

	public Double getMarch() {
		return march;
	}

	public void setMarch(Double march) {
		this.march = march;
	}

	public Double getApril() {
		return april;
	}

	public void setApril(Double april) {
		this.april = april;
	}

	public Double getMay() {
		return may;
	}

	public void setMay(Double may) {
		this.may = may;
	}

	public Double getJune() {
		return june;
	}

	public void setJune(Double june) {
		this.june = june;
	}

	public Double getJuly() {
		return july;
	}

	public void setJuly(Double july) {
		this.july = july;
	}

	public Double getAugust() {
		return august;
	}

	public void setAugust(Double august) {
		this.august = august;
	}

	public Double getSeptember() {
		return september;
	}

	public void setSeptember(Double september) {
		this.september = september;
	}

	public Double getOctober() {
		return october;
	}

	public void setOctober(Double october) {
		this.october = october;
	}

	public Double getNovember() {
		return november;
	}

	public void setNovember(Double november) {
		this.november = november;
	}

	public Double getDecember() {
		return december;
	}

	public void setDecember(Double december) {
		this.december = december;
	}

	public Double getYearlyBudget() {
		return yearlyBudget;
	}

	public void setYearlyBudget(Double yearlyBudget) {
		this.yearlyBudget = yearlyBudget;
	}
	
	

}
