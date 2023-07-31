package com.cognixia.jump.controller;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognixia.jump.exceptions.IllegalOptionException;
import com.cognixia.jump.model.Credentials;
import com.cognixia.jump.model.RegisteredPeople;
import com.cognixia.jump.model.Student;
import com.cognixia.jump.model.Teacher;
import com.cognixia.jump.repository.RegisteredPeopleRepository;
import com.cognixia.jump.repository.StudentRepository;
import com.cognixia.jump.repository.TeacherRepository;
import com.cognixia.jump.util.ColorUtility;
import com.cognixia.jump.util.MenuUtil;

@Component
public class Gradebook {
	
	public static Scanner sc = new Scanner(System.in);
	
	@Autowired
	private TeacherController teacherController;
	
	@Autowired
	private StudentController studentController;
	
	@Autowired
	private TeacherRepository tRepo;
	
	@Autowired
	private StudentRepository sRepo;
	
	@Autowired
	private RegisteredPeopleRepository rRepo;
	
	

	public void entryPoint() {
		do {
			try {
				MenuUtil.greeting();
				int option = sc.nextInt();
				sc.nextLine();
				
				switch (option) {
					case 1:
						// Login
						System.out.print(ColorUtility.YELLOW_TEXT + "\nEnter your email:" + ColorUtility.CYAN_TEXT);
						Credentials cred = new Credentials();
						cred.setEmail(sc.nextLine().toLowerCase());
						
						if (cred.getEmail() == null || cred.getEmail().isEmpty() || cred.getEmail().isBlank() ) {
							System.out.println(ColorUtility.RED_TEXT + "\nInputted email is invalid. Please try again.\n");
							continue;
						}
						
						Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
						Matcher matcher = emailPattern.matcher(cred.getEmail());
						if (!matcher.find()) {
							System.out.println(ColorUtility.RED_TEXT + "\nInputted email is invalid. Please try again.\n");
							continue;
						}
						
						System.out.print(ColorUtility.YELLOW_TEXT + "\nPassword: " + ColorUtility.CYAN_TEXT);
						cred.setPassword(sc.nextLine());
						
						if (cred.getPassword() == null || cred.getPassword().isEmpty() || cred.getPassword().isBlank() ) {
							System.out.println(ColorUtility.RED_TEXT + "\nInputted password is empty/blank. Please try again.\n");
							continue;
						}
						
						Optional<Teacher> teacher = tRepo.findByEmail(cred.getEmail());
						if (!teacher.isEmpty()) {
							if (cred.getPassword().equals(teacher.get().getPassword())) {
								// CONTINUE TO TEACHER PORTAL
								if (teacher.get().isFirstTimeLogin()) {
									teacherController.firstTimeLogin(teacher.get());
								}
								teacherController.init(teacher.get());
								continue;
							} else {
								System.out.println(ColorUtility.RED_TEXT + "\nLogin info incorrect. Try again!\n");
								continue;
							}
						}
						
						Optional<Student> student = sRepo.findByEmail(cred.getEmail());
						if (!student.isEmpty()) {
							if (cred.getPassword().equals(student.get().getPassword())) {
								// CONTINUE TO STUDENT PORTAL
								if (student.get().isFirstTimeLogin()) {
									studentController.firstTimeLogin(student.get());
								}
								studentController.init(student.get());
								continue;
							} else {
								System.out.println(ColorUtility.RED_TEXT + "\nLogin info incorrect. Try again!\n");
								continue;
							}
						}
						
						System.out.println(ColorUtility.RED_TEXT + "\nLogin info incorrect. Try again!\n");
						break;
					case 2:
						// New User
						boolean userCreated = false;
						System.out.println(ColorUtility.YELLOW_TEXT + "\nEnter your first and last name:");
						String name = sc.nextLine().toLowerCase();
						List<RegisteredPeople> registered = rRepo.findAll();
						for (RegisteredPeople r : registered) {
							if (r.getName().equals(name)) {
								System.out.println(ColorUtility.YELLOW_TEXT + "\nEnter your date of birth [mm/dd/yyyy]:");
								String dob = sc.nextLine();
								String regex = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";
							    //Creating a pattern object
							    Pattern pattern = Pattern.compile(regex);
							    Matcher matches = pattern.matcher(dob);
							    if (matches.matches()) {
							    	if (r.getDob().equals(dob)) {
							    		String genPass = RandomStringUtils.randomAlphanumeric(8);
							    		if (r.getType().equals("Student")) {
							    			Student newStudent = new Student();
							    			newStudent.setId(null);
							    			newStudent.setName(r.getName());
							    			newStudent.setEmail("temp");
							    			newStudent.setPassword(genPass);
							    			newStudent.setFirstTimeLogin(true);
							    			newStudent = sRepo.save(newStudent);
							    			String lastName = name.split(" ")[1];
							    			newStudent.setEmail(lastName + newStudent.getId() + "@thisschool.com");
							    			sRepo.save(newStudent);
							    			r.setFirst_login(false);
							    			rRepo.save(r);
							    			System.out.println(ColorUtility.GREEN_TEXT + "\nAccount Registered!");
							    			System.out.println("Temporary Password: " + genPass + "\n");
							    			userCreated = true;
							    			break;
							    		} else {
							    			Teacher newTeacher = new Teacher();
							    			newTeacher.setId(null);
							    			newTeacher.setEmail("temp");
							    			newTeacher.setPassword(genPass);
							    			newTeacher.setFirstTimeLogin(true);
							    			newTeacher = tRepo.save(newTeacher);
							    			String lastName = name.split(" ")[1];
							    			newTeacher.setEmail(lastName + newTeacher.getId() + "@thisschool.com");
							    			tRepo.save(newTeacher);
							    			r.setFirst_login(false);
							    			rRepo.save(r);
							    			System.out.println(ColorUtility.GREEN_TEXT + "\nAccount Registered!");
							    			System.out.println("Temporary Password: " + genPass + "\n");
							    			userCreated = true;
							    			break;
							    		}
							    	}
							    }
							}
						}
						if (!userCreated) {
							System.out.println(ColorUtility.RED_TEXT + "\nUser not found under that name. Try Again!\n");
						}
						break;
					case 3:
						return;
					default:
						throw new IllegalOptionException();
				}
		
			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nInput is invalid. Try again!\n");
				sc.nextLine();
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot an option. Try again!");
			}
		} while(true);
		
	}
}
