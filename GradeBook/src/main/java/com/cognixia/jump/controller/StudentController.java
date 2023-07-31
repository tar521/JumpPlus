package com.cognixia.jump.controller;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognixia.jump.exceptions.IllegalOptionException;
import com.cognixia.jump.model.Grade;
import com.cognixia.jump.model.Schedule;
import com.cognixia.jump.model.Student;
import com.cognixia.jump.service.CourseService;
import com.cognixia.jump.service.GradeService;
import com.cognixia.jump.service.ScheduleService;
import com.cognixia.jump.service.UserService;
import com.cognixia.jump.util.ColorUtility;
import com.cognixia.jump.util.GradeUtil;
import com.cognixia.jump.util.MenuUtil;

@Component
public class StudentController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GradeService gradeService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private CourseService courseService;
	
	private Student student;
	
	public void firstTimeLogin(Student student) {
		do {
			System.out.println(ColorUtility.GREEN_TEXT + "\nPlease set your password:");
			String pass1 = Gradebook.sc.nextLine();
			System.out.println("Please re-enter your password:");
			String pass2 = Gradebook.sc.nextLine();
			if (pass1.equals(pass2) ) {
				student.setPassword(pass2);
				student.setFirstTimeLogin(false);
				userService.saveStudent(student);
				return;
			} else {
				System.out.println(ColorUtility.RED_TEXT + "\nPassword doesn't match, try again!\n");
			}
		} while(true);
	}

	public void init(Student student) {
		this.student = student;
		this.student.setGrades(gradeService.loadGradesStudent(this.student));
		this.student.setSchedule(scheduleService.loadSchedule(this.student));
		if (this.student.getGrades() == null) {
			this.student.setGrades(new ArrayList<Grade>());
		}
		if (this.student.getSchedule() == null) {
			this.student.setSchedule(new ArrayList<Schedule>());
		}
		session();
	}

	private void session() {
		List<GradeUtil> grades = new ArrayList<GradeUtil>();
		if (!student.getSchedule().isEmpty()) {
			for (Schedule s : student.getSchedule()) {
				GradeUtil calcGrades = new GradeUtil();
				calcGrades.setStudentId(student.getId());
				calcGrades.setStudentName(student.getName());
				calcGrades.setCourseName(courseService.getCourseById(s.getCourse()).getName());
				calcGrades.populateGradeStudent(gradeService.loadGradesByCourseAndStudent(s, student));
				grades.add(calcGrades);
			}
		}
		do {
			try {
				if (!grades.isEmpty()) {
					MenuUtil.printExpandedGrades(grades);
				} else {
					System.out.println(ColorUtility.RED_TEXT + "\n*** No grades! ***\n");
				}
				System.out.println(ColorUtility.YELLOW_TEXT + "\n[Type 'exit' to logout]");
				String exit = Gradebook.sc.nextLine();
				if (exit.equalsIgnoreCase("exit")) {
					student = null;
					return;
				} else {
					throw new IllegalOptionException();
				}
				

			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again!\n");
			}
		} while(true);
		
	}

}
