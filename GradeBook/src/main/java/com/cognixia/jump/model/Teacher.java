package com.cognixia.jump.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Teacher extends User {

	@OneToMany(mappedBy = "teacher")
	private List<Course> courses;
	
	@OneToMany(mappedBy = "teacher")
	private List<Grade> grades;
	
	public Teacher() {
		super();
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public List<Grade> getGrades() {
		return grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}
	
}
