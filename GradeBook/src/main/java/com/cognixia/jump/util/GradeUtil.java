package com.cognixia.jump.util;

import java.util.ArrayList;
import java.util.List;

import com.cognixia.jump.model.Grade;

public class GradeUtil {
	
	private Integer studentId;
	private String studentName;
	private String courseName;
	
	private double hw1Grade;
	private double hw2Grade;
	private double hw3Grade;
	private double hw4Grade;
	private double quiz1Grade;
	private double quiz2Grade;
	private double midtermGrade;
	private double finalGrade;
	private double courseGrade;
	
	private int hw1TurnedIn;
	private int hw2TurnedIn;
	private int hw3TurnedIn;
	private int hw4TurnedIn;
	private int quiz1TurnedIn;
	private int quiz2TurnedIn;
	private int midtermTurnedIn;
	private int finalTurnedIn;
	
	public GradeUtil() {
		
	}
	
	public GradeUtil(int value) {
		hw1Grade = value;
		hw2Grade = value;
		hw3Grade = value;
		hw4Grade = value;
		quiz1Grade = value;
		quiz2Grade = value;
		midtermGrade = value;
		finalGrade = value;
		courseGrade = value;
		hw1TurnedIn = value;
		hw2TurnedIn = value;
		hw3TurnedIn = value;
		hw4TurnedIn = value;
		quiz1TurnedIn = value;
		quiz2TurnedIn = value;
		midtermTurnedIn = value;
		finalTurnedIn = value;
	}

	// GRADE LIST IS RECEIVED EXPLICITLY BY CLASS
	public void populateGradeStudent(List<Grade> grades) {
		List<Grade> hw1 = new ArrayList<Grade>();
		List<Grade> hw2 = new ArrayList<Grade>();
		List<Grade> hw3 = new ArrayList<Grade>();
		List<Grade> hw4 = new ArrayList<Grade>();
		Grade quiz1 = null;
		Grade quiz2 = null;
		Grade midterm = null;
		Grade finalTest = null;
		hw1TurnedIn = 0;
		hw2TurnedIn = 0;
		hw3TurnedIn = 0;
		hw4TurnedIn = 0;
		quiz1TurnedIn = 0;
		quiz2TurnedIn = 0;
		midtermTurnedIn = 0;
		finalTurnedIn = 0;
		
		for (Grade g : grades) {
			String[] temp = g.getCategory().toString().split("_");
			switch (temp[0]) {
				case "HOMEWORK":
					switch (temp[1]) {
						case "1":
							hw1.add(g);
							hw1TurnedIn += 1;
							break;
						case "2":
							hw2.add(g);
							hw2TurnedIn += 1;
							break;
						case "3":
							hw3.add(g);
							hw3TurnedIn += 1;
							break;
						case "4":
							hw4.add(g);
							hw4TurnedIn += 1;
							break;
					}
					break;
				case "QUIZ":
					switch (temp[1]) {
						case "1":
							quiz1TurnedIn += 1;
							quiz1 = g;
							break;
						case "2":
							quiz2TurnedIn += 1;
							quiz2 = g;
							break;
						}
					break;
				case "MIDTERM":
					midtermTurnedIn += 1;
					midterm = g;
					break;
				case "FINAL":
					finalTurnedIn += 1;
					finalTest = g;
					break;
			}
		}
		
		// POPULATE GRADES
		hw1Grade = calcHw(hw1);
		hw2Grade = calcHw(hw2);
		hw3Grade = calcHw(hw3);
		hw4Grade = calcHw(hw4);
		quiz1Grade = (quiz1 == null) ? 0 : quiz1.getResult();
		quiz2Grade = (quiz2 == null) ? 0 : quiz2.getResult();
		midtermGrade = (midterm == null) ? 0 : midterm.getResult();
		finalGrade = (finalTest == null) ? 0 : finalTest.getResult();
		courseGrade = ((hw1Grade + hw2Grade + hw3Grade + hw4Grade)/4.0 * GradeConstants.hwWeight)
				+ ((quiz2Grade + quiz1Grade)/2 * GradeConstants.quizWeight)
				+ ((midtermGrade + finalGrade)/2 * GradeConstants.testWeight);
		
		
	}
	
	private double calcHw(List<Grade> homework) {
		double sum = 0;
		for (Grade h : homework) {
			sum += h.getResult();
		}
		return sum / 3.0;
	}
	
	

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public double getHw1Grade() {
		return hw1Grade;
	}

	public void setHw1Grade(double hw1Grade) {
		this.hw1Grade = hw1Grade;
	}

	public double getHw2Grade() {
		return hw2Grade;
	}

	public void setHw2Grade(double hw2Grade) {
		this.hw2Grade = hw2Grade;
	}

	public double getHw3Grade() {
		return hw3Grade;
	}

	public void setHw3Grade(double hw3Grade) {
		this.hw3Grade = hw3Grade;
	}

	public double getHw4Grade() {
		return hw4Grade;
	}

	public void setHw4Grade(double hw4Grade) {
		this.hw4Grade = hw4Grade;
	}

	public double getQuiz1Grade() {
		return quiz1Grade;
	}

	public void setQuiz1Grade(double quiz1Grade) {
		this.quiz1Grade = quiz1Grade;
	}

	public double getQuiz2Grade() {
		return quiz2Grade;
	}

	public void setQuiz2Grade(double quiz2Grade) {
		this.quiz2Grade = quiz2Grade;
	}

	public double getMidtermGrade() {
		return midtermGrade;
	}

	public void setMidtermGrade(double midtermGrade) {
		this.midtermGrade = midtermGrade;
	}

	public double getFinalGrade() {
		return finalGrade;
	}

	public void setFinalGrade(double finalGrade) {
		this.finalGrade = finalGrade;
	}

	public double getCourseGrade() {
		return courseGrade;
	}

	public void setCourseGrade(double courseGrade) {
		this.courseGrade = courseGrade;
	}

	public int getHw1TurnedIn() {
		return hw1TurnedIn;
	}

	public void setHw1TurnedIn(int hw1TurnedIn) {
		this.hw1TurnedIn = hw1TurnedIn;
	}

	public int getHw2TurnedIn() {
		return hw2TurnedIn;
	}

	public void setHw2TurnedIn(int hw2TurnedIn) {
		this.hw2TurnedIn = hw2TurnedIn;
	}

	public int getHw3TurnedIn() {
		return hw3TurnedIn;
	}

	public void setHw3TurnedIn(int hw3TurnedIn) {
		this.hw3TurnedIn = hw3TurnedIn;
	}

	public int getHw4TurnedIn() {
		return hw4TurnedIn;
	}

	public void setHw4TurnedIn(int hw4TurnedIn) {
		this.hw4TurnedIn = hw4TurnedIn;
	}

	public int getQuiz1TurnedIn() {
		return quiz1TurnedIn;
	}

	public void setQuiz1TurnedIn(int quiz1TurnedIn) {
		this.quiz1TurnedIn = quiz1TurnedIn;
	}

	public int getQuiz2TurnedIn() {
		return quiz2TurnedIn;
	}

	public void setQuiz2TurnedIn(int quiz2TurnedIn) {
		this.quiz2TurnedIn = quiz2TurnedIn;
	}

	public int getMidtermTurnedIn() {
		return midtermTurnedIn;
	}

	public void setMidtermTurnedIn(int midtermTurnedIn) {
		this.midtermTurnedIn = midtermTurnedIn;
	}

	public int getFinalTurnedIn() {
		return finalTurnedIn;
	}

	public void setFinalTurnedIn(int finalTurnedIn) {
		this.finalTurnedIn = finalTurnedIn;
	}
	
	
}
