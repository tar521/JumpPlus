package com.cognixia.jump.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Student extends User {

	@OneToMany(mappedBy = "student")
	private List<Schedule> schedule;
	
	@OneToMany(mappedBy = "student")
	private List<Grade> grades;
	
	public Student() {
		super();
	}

	public List<Schedule> getSchedule() {
		return schedule;
	}

	public void setSchedule(List<Schedule> schedule) {
		this.schedule = schedule;
	}

	public List<Grade> getGrades() {
		return grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}
	
	
}
