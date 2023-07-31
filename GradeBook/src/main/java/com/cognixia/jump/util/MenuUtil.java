package com.cognixia.jump.util;

import java.util.List;

import com.cognixia.jump.model.Course;
import com.cognixia.jump.model.Teacher;
import com.cognixia.jump.model.User;

public class MenuUtil {

	public static void greeting() {
		System.out.println(ColorUtility.YELLOW_TEXT);
		System.out.println("-----------------------------------------------");
		System.out.println("|  Welcome to Some School's Gradebook System  |");
		System.out.println("-----------------------------------------------");
		System.out.println("   1: Login  2: New User  3: Exit" + ColorUtility.TEXT_RESET);
	}
	
	public static void exitMessage() {
		System.out.println(ColorUtility.YELLOW_TEXT);
		System.out.println("-------------------------------------------------");
		System.out.println("|  Thanks for using the Contact Manager System  |");
		System.out.println("-------------------------------------------------");
		System.out.println(ColorUtility.TEXT_RESET);
	}

	public static void session() {
		System.out.println(ColorUtility.YELLOW_TEXT);
		System.out.println("\nSelect an Option: ");
		System.out.println("  1. View Course and Grades");
		System.out.println("  2. Add Course");
		System.out.println("  3. Remove Course");
		System.out.println("  4. Exit (Logout)");
		System.out.print("Option ID: " + ColorUtility.CYAN_TEXT);
	}
	
	public static void courseList(Teacher teacher) {
		System.out.println(ColorUtility.YELLOW_TEXT);
		
		if (teacher.getCourses().size() > 0) {
			System.out.printf("---------------------------------------------------------------%n");
			System.out.printf("|        Your Courses                                         |%n");
			System.out.printf("---------------------------------------------------------------%n");
			System.out.printf("| %-3s | %-25s | %-25s |%n", "ID", "Course Name", "Teacher Email");
			System.out.printf("---------------------------------------------------------------%n");
			
			for (Course c : teacher.getCourses()) {
				System.out.printf("| %-3d | %-25s | %-25s |%n", c.getId().intValue(), c.getName(), teacher.getEmail());
			}
			
			System.out.printf("---------------------------------------------------------------%n");
		}
	}
	
	public static void printGrades(List<GradeUtil> grades, String name) {
		System.out.println(ColorUtility.YELLOW_TEXT + "\n\tStudent Grades for " + name);
		System.out.printf("--------------------------------------------%n");
		System.out.printf("| %-3s | %-25s | %-6s |%n", "ID", "Student Name", "Grade");
		System.out.printf("--------------------------------------------%n");
		for (GradeUtil gu : grades) {
			System.out.printf("| %3d | %-25s | %6.2f |%n", gu.getStudentId(), gu.getStudentName(), gu.getCourseGrade());
		}
		System.out.printf("--------------------------------------------%n");
		
	}
	
	public static void printExpandedGrades(List<GradeUtil> grades, String name) {
		System.out.println(ColorUtility.YELLOW_TEXT + "\n*** Student Grades For " + name + " ***");
		System.out.printf("-----------------------------------------------------------------------------------------------------------------%n");
		System.out.printf("|  Turned In  | Homework 1 | Quiz 1 | Homework 2 | Midterm | Homework 3 | Quiz 2 | Homework 4 | Final  | Course |%n");
		System.out.printf("| ID and Name |   Grade    | Grade  |   Grade    |  Grade  |   Grade    | Grade  |   Grade    | Grade  | Grade  |%n");
		for (GradeUtil g : grades) {
			System.out.printf("-----------------------------------------------------------------------------------------------------------------%n");
			System.out.printf("|             |    %1d/3     |  %1d/1   |    %1d/3     |   %1d/1   |    %1d/3     |  %1d/1   |    %1d/3     |  %1d/1   |        |%n", g.getHw1TurnedIn(), g.getQuiz1TurnedIn(), g.getHw2TurnedIn(), g.getMidtermTurnedIn(), g.getHw3TurnedIn(), g.getQuiz2TurnedIn(), g.getHw4TurnedIn(), g.getFinalTurnedIn());
			System.out.printf("| %3d %-7s |   %6.2f   | %6.2f |   %6.2f   |  %6.2f |   %6.2f   | %6.2f |   %6.2f   | %6.2f | %6.2f |%n", g.getStudentId(), g.getStudentName().substring(0,7), g.getHw1Grade(), g.getQuiz1Grade(), g.getHw2Grade(), g.getMidtermGrade(), g.getHw3Grade(), g.getQuiz2Grade(), g.getHw4Grade(), g.getFinalGrade(), g.getCourseGrade());
		}
		System.out.printf("-----------------------------------------------------------------------------------------------------------------%n");
	}
	
	public static void printExpandedGrades(List<GradeUtil> grades) {
		System.out.println(ColorUtility.YELLOW_TEXT + "\n*** Student Grades ***");
		System.out.printf("-----------------------------------------------------------------------------------------------------------------%n");
		System.out.printf("|  Turned In  | Homework 1 | Quiz 1 | Homework 2 | Midterm | Homework 3 | Quiz 2 | Homework 4 | Final  | Course |%n");
		System.out.printf("| ID and Name |   Grade    | Grade  |   Grade    |  Grade  |   Grade    | Grade  |   Grade    | Grade  | Grade  |%n");
		for (GradeUtil g : grades) {
			System.out.printf("-----------------------------------------------------------------------------------------------------------------%n");
			System.out.printf("| %-11s |    %1d/3     |  %1d/1   |    %1d/3     |   %1d/1   |    %1d/3     |  %1d/1    |    %1d/3     |  %1d/1  |        |%n", g.getCourseName(), g.getHw1TurnedIn(), g.getQuiz1TurnedIn(), g.getHw2TurnedIn(), g.getMidtermTurnedIn(), g.getHw3TurnedIn(), g.getQuiz2TurnedIn(), g.getHw4TurnedIn(), g.getFinalTurnedIn());
			System.out.printf("| %3d         |   %6.2f   | %6.2f |   %6.2f   |  %6.2f |   %6.2f   | %6.2f |   %6.2f   | %6.2f | %6.2f |%n", g.getStudentId(), g.getStudentName(), g.getHw1Grade(), g.getQuiz1Grade(), g.getHw2Grade(), g.getMidtermGrade(), g.getHw3Grade(), g.getQuiz2Grade(), g.getHw4Grade(), g.getFinalGrade(), g.getCourseGrade());
		}
		System.out.printf("-----------------------------------------------------------------------------------------------------------------%n");
	}
	
}
