package com.cognixia.jump.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognixia.jump.exceptions.IllegalOptionException;
import com.cognixia.jump.model.Course;
import com.cognixia.jump.model.Grade;
import com.cognixia.jump.model.Grade.Category;
import com.cognixia.jump.model.Schedule;
import com.cognixia.jump.model.Student;
import com.cognixia.jump.model.Teacher;
import com.cognixia.jump.service.CourseService;
import com.cognixia.jump.service.GradeService;
import com.cognixia.jump.service.ScheduleService;
import com.cognixia.jump.service.UserService;
import com.cognixia.jump.util.ColorUtility;
import com.cognixia.jump.util.GradeUtil;
import com.cognixia.jump.util.MenuUtil;

@Component
public class TeacherController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GradeService gradeService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	private Teacher teacher;
	
	private String sortMethod;
	
	public void firstTimeLogin(Teacher teacher) {
		do {
			System.out.println(ColorUtility.GREEN_TEXT + "\nPlease set your password:");
			String pass1 = Gradebook.sc.nextLine();
			System.out.println("Please re-enter your password:");
			String pass2 = Gradebook.sc.nextLine();
			if (pass1.equals(pass2) ) {
				teacher.setPassword(pass2);
				teacher.setFirstTimeLogin(false);
				userService.saveTeacher(teacher);
				return;
			} else {
				System.out.println(ColorUtility.RED_TEXT + "\nPassword doesn't match, try again!\n");
			}
		} while(true);
	}

	public void init(Teacher teacher) {
		sortMethod = "name";
		this.teacher = teacher;
		this.teacher.setGrades(gradeService.loadGradesTeacher(this.teacher));
		this.teacher.setCourses(courseService.loadCourses(this.teacher));
		if (this.teacher.getGrades() == null) {
			this.teacher.setGrades(new ArrayList<Grade>());
		}
		if (this.teacher.getCourses() == null) {
			this.teacher.setCourses(new ArrayList<Course>());
		}
		session();
	}

	private void session() {
		do {
			try {
				if (teacher.getCourses().isEmpty()) {
					System.out.println(ColorUtility.RED_TEXT + "\n*** No assigned courses to show. ***\n" + ColorUtility.TEXT_RESET);
				} else {
					MenuUtil.courseList(teacher);
				}
				
				MenuUtil.session();
				int option = Gradebook.sc.nextInt();
				Gradebook.sc.nextLine();
				switch(option) {
					case 1:
						if (!teacher.getCourses().isEmpty()) {
							viewCourseAndGrades();
						} else {
							System.out.println(ColorUtility.RED_TEXT + "\nYou don't have any courses. Please add courses first.\n" + ColorUtility.TEXT_RESET);
						}
						break;
					case 2:
						addCourse();
						break;
					case 3:
						if(!teacher.getCourses().isEmpty()) {
							removeCourse();
						} else {
							System.out.println(ColorUtility.RED_TEXT + "\nNo courses to remove! Please add a course.\n" + ColorUtility.TEXT_RESET);
						}
						
						break;
					case 4:
						return;
					default:
						throw new IllegalOptionException();
				}

			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again!\n");
				Gradebook.sc.nextLine();
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again!\n");
			}
		} while(true);
		
	}

	private void removeCourse() {
		do {
			try {
				MenuUtil.courseList(teacher);
				System.out.println(ColorUtility.YELLOW_TEXT + "Enter ID of course to remove:" + ColorUtility.CYAN_TEXT);
				int removeId = Gradebook.sc.nextInt();
				Gradebook.sc.nextLine();
				
				for (Course c : teacher.getCourses()) {
					if (c.getId().intValue() == removeId) {
						teacher.getCourses().remove(c);
						courseService.removeCourse(c);
						scheduleService.removeScheduleByCourse(c);
						gradeService.removeGradesForCourse(c);
						teacher.setGrades(gradeService.loadGradesTeacher(teacher));
						System.out.println(ColorUtility.GREEN_TEXT + "\nCourse Removed!\n");
						return;
					}
				}
				
				throw new IllegalOptionException();
				
			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again!\n");
				Gradebook.sc.nextLine();
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again!\n");
			}
		} while(true);
		
	}

	private void viewCourseAndGrades() {
		do {
			try {
				MenuUtil.courseList(teacher);
				System.out.print(ColorUtility.YELLOW_TEXT + "\nEnter ID of course to view/edit: " + ColorUtility.CYAN_TEXT);
				int idToView = Gradebook.sc.nextInt();
				Gradebook.sc.nextLine();
				Course found = null;
				
				for (Course c : teacher.getCourses()) {
					if (idToView == c.getId().intValue()) {
						found = c;
					}
				}
				if (found != null) {
					courseViewer(found);
					do {
						System.out.println(ColorUtility.YELLOW_TEXT + "\nDo you want to view/edit another course? [Y/n]");
						String answer = Gradebook.sc.nextLine();
						if (answer.equalsIgnoreCase("y")) {
							continue;
						} else if (answer.equalsIgnoreCase("n")) {
							return;
						} else {
							System.out.println(ColorUtility.RED_TEXT + "\nNot a valid answer. Try again!");
						}
					} while(true);
				} else {
					throw new IllegalOptionException();
				}
				
			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid id. Please try again!\n");
				Gradebook.sc.nextLine();
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid id. Please try again!\n");
			}
		} while(true);
		
	}

	private void addCourse() {
		List<Course> offeredCourses = courseService.getCourseList();
		do {
			try {
				System.out.println(ColorUtility.YELLOW_TEXT + "\n-------------------");
				System.out.println("| Offered Courses |");
				System.out.println("-------------------");
				for (Course c : offeredCourses) {
					System.out.println("  " + c.getId() + "\t" + c.getName());
				}
				System.out.print("\nEnter an ID to add course: " + ColorUtility.CYAN_TEXT);
				int findId = Gradebook.sc.nextInt();
				Gradebook.sc.nextLine();
				
				for (Course c : offeredCourses) {
					if (findId == c.getId().intValue()) {
						c.setId(null);
						c.setTeacher(teacher);
						Course addedCourse = courseService.addCourse(c);
						teacher.getCourses().add(addedCourse);
						System.out.println(ColorUtility.GREEN_TEXT + "\nCourse added!\n" + ColorUtility.TEXT_RESET);
						return;
					}
				}
				
				throw new IllegalOptionException();
				
			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid id. Please try again!\n");
				Gradebook.sc.nextLine();
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid id. Please try again!\n");
			}
		} while(true);
	}
	
	private void courseViewer(Course found) {
		// get students and grades
		found.setSchedule(scheduleService.getScheduleByCourse(found));
		List<GradeUtil> grades = new ArrayList<GradeUtil>();
		Map<Integer, Student> students = new ConcurrentHashMap<Integer, Student>();
		if (!found.getSchedule().isEmpty()) {
			for (Schedule s : found.getSchedule()) {
				Student added = userService.getStudentById(s.getStudent());
				added.setGrades(gradeService.loadGradesByCourseAndStudent(s, added));
				GradeUtil calcGrades = new GradeUtil();
				calcGrades.setStudentId(added.getId());
				calcGrades.setStudentName(added.getName());
				calcGrades.populateGradeStudent(added.getGrades());
				grades.add(calcGrades);
				students.put(added.getId(), added);
			}
		}
		
		do {
			try {
				if (!grades.isEmpty()) {
					sortGradeUtil(grades);
					MenuUtil.printGrades(grades, found.getName());
					double average = findAverage(new ArrayList<GradeUtil>(grades));
					double median = findMedian(new ArrayList<GradeUtil>(grades));
					System.out.printf("Average: %6.2f\tMedian: %6.2f%n", average, median);
				} else {
					System.out.println(ColorUtility.YELLOW_TEXT + "\n*** No one enrolled ***");
				}
				
				System.out.println("\nSelect action for gradebook: ");
				System.out.println("  1. Change Sorting [Name, Grade] - Current: " + sortMethod);
				System.out.println("  2. Update Students Grades");
				System.out.println("  3. Remove Student");
				System.out.println("  4. Add Student");
				System.out.println("  5. Return to Menu");
				System.out.print("Option: " + ColorUtility.CYAN_TEXT);
				int option = Gradebook.sc.nextInt();
				Gradebook.sc.nextLine();
				
				switch(option) {
				case 1:
					if (sortMethod.equals("name")) {
						sortMethod = "grade";
					} else {
						sortMethod = "name";
					}
					break;
				case 2:
					if (!students.isEmpty()) {
						gradeWizard(found, grades, students);
					} else {
						System.out.println(ColorUtility.RED_TEXT + "\nNo students to remove!\n");
					}
					break;
				case 3:
					if (!students.isEmpty()) {
						removeStudent(found, students, grades);
					} else {
						System.out.println(ColorUtility.RED_TEXT + "\nNo students to remove!\n");
					}
					break;
				case 4:
					Student newStudent = addStudent(found, students);
					GradeUtil tempGrade = new GradeUtil(0);
					tempGrade.setStudentId(newStudent.getId());
					tempGrade.setStudentName(newStudent.getName());
					grades.add(tempGrade);
					break;
				case 5:
					return;
				default:
					throw new IllegalOptionException();
				}

			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again!\n");
				Gradebook.sc.nextLine();
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again!\n");
			}
		} while(true);
		
	}

	private void removeStudent(Course found, Map<Integer, Student> students, List<GradeUtil> grades) {
		do {
			try {
				System.out.println(ColorUtility.YELLOW_TEXT + "\n*** SELECT STUDENT TO REMOVE ***");
				System.out.println("ID:  \tStudent Name:");
				for (Map.Entry<Integer, Student> entry : students.entrySet()) {
					System.out.println(" " + entry.getKey().toString() + "\t" + entry.getValue().getName());
				}
				System.out.print("\nEnter Student ID: " + ColorUtility.CYAN_TEXT);
				int studId = Gradebook.sc.nextInt();
				Gradebook.sc.nextLine();
				
				if (students.containsKey(studId)) {
					Student removed = students.remove(studId);
					for (GradeUtil g : grades) {
						if (g.getStudentId().intValue() == studId) {
							grades.remove(g);
							break;
						}
					}
					for (Schedule s : found.getSchedule()) {
						if (s.getStudent().getId().intValue() == studId) {
							found.getSchedule().remove(s);
							scheduleService.removeSchedule(s);
							break;
						}
					}
					gradeService.removeStudentGradesForCourse(found, removed);
					System.out.println(ColorUtility.GREEN_TEXT + "\nStudent Removed!\n");
					teacher.setGrades(gradeService.loadGradesTeacher(teacher));
					return;
				} else {
					throw new IllegalOptionException();
				}

			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again!\n");
				Gradebook.sc.nextLine();
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again!\n");
			}
		} while(true);
		
	}

	private Student addStudent(Course found, Map<Integer, Student> enrolled) {
		Map<Integer, Student> notEnrolledStudents = userService.getAllStudents();
		if (!enrolled.isEmpty()) {
			for (Map.Entry<Integer, Student> entry : enrolled.entrySet()) {
				if (notEnrolledStudents.containsKey(entry.getKey())) {
					notEnrolledStudents.remove(entry.getKey());
				}
			}
		}
		do {
			try {
				System.out.println(ColorUtility.YELLOW_TEXT + "\n*** SELECT STUDENT TO ENROLL ***");
				System.out.println("ID:  \tStudent Name:");
				for (Map.Entry<Integer, Student> entry : notEnrolledStudents.entrySet()) {
					System.out.println(" " + entry.getKey().toString() + "\t" + entry.getValue().getName());
				}
				System.out.print("\nEnter Student ID: " + ColorUtility.CYAN_TEXT);
				int studId = Gradebook.sc.nextInt();
				Gradebook.sc.nextLine();
				
				if (notEnrolledStudents.containsKey(studId)) {
					Schedule newSchedule = new Schedule();
					newSchedule.setId(null);
					newSchedule.setCourse(found);
					newSchedule.setStudent(notEnrolledStudents.get(studId));
					found.getSchedule().add(scheduleService.addSchedule(newSchedule));
					Student temp = notEnrolledStudents.get(studId);
					temp.setGrades(new ArrayList<Grade>());
					enrolled.put(studId, temp);
					System.out.println(ColorUtility.GREEN_TEXT + "\nStudent enrolled!\n" + ColorUtility.TEXT_RESET);
					teacher.setGrades(gradeService.loadGradesTeacher(teacher));
					return temp;
				} else {
					throw new IllegalOptionException();
				}
				
			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again!\n");
				Gradebook.sc.nextLine();
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again!\n");
			}
		} while(true);
	}

	private void gradeWizard(Course found, List<GradeUtil> grades, Map<Integer, Student> students) {
		do {
			try {
				MenuUtil.printExpandedGrades(grades, found.getName());
				System.out.println("\n[To exit input -1]");
				System.out.print("Input Student ID: " + ColorUtility.CYAN_TEXT);
				int studId = Gradebook.sc.nextInt();
				Gradebook.sc.nextLine();
				
				if (studId == -1) {
					return;
				}
				
				if (students.containsKey(studId)) {
					// ADD GRADES FOR STUDENT
					GradeUtil gradeEdit = null;
					for (GradeUtil g : grades) {
						if (g.getStudentId().intValue() == studId) {
							gradeEdit = g;
							break;
						}
					}
					System.out.println(ColorUtility.YELLOW_TEXT + "\nAssignments not completed/entered for " + gradeEdit.getCourseName() + ":");
					int[] assignmentsMissing = {0, 0, 0, 0, 0, 0, 0, 0};
					if (gradeEdit.getHw1TurnedIn() < 3) {
						assignmentsMissing[0] = 3 - gradeEdit.getHw1TurnedIn();
					}
					if (gradeEdit.getHw2TurnedIn() < 3) {
						assignmentsMissing[2] = 3 - gradeEdit.getHw2TurnedIn();
					}
					if (gradeEdit.getHw3TurnedIn() < 3) {
						assignmentsMissing[4] = 3 - gradeEdit.getHw3TurnedIn();
					}
					if (gradeEdit.getHw4TurnedIn() < 3) {
						assignmentsMissing[6] = 3 - gradeEdit.getHw4TurnedIn();
					}
					if (gradeEdit.getQuiz1TurnedIn() < 1) {
						assignmentsMissing[1] = 1;
					}
					if (gradeEdit.getQuiz2TurnedIn() < 1) {
						assignmentsMissing[5] = 1;
					}
					if (gradeEdit.getMidtermTurnedIn() < 1) {
						assignmentsMissing[3] = 1;
					}
					if (gradeEdit.getFinalTurnedIn() < 1) {
						assignmentsMissing[7] = 1;
					}
					System.out.printf(ColorUtility.YELLOW_TEXT + "%-10s:\t%-2s:\t%-10s%n", "Category", "ID", "# Missing:");
					for (int i = 0; i < assignmentsMissing.length; i++) {
						if (assignmentsMissing[i] > 0) {
							System.out.printf("%11s\t%2d\t%-2d%n", Category.values()[i].name(), Category.values()[i].ordinal(), assignmentsMissing[i]);
						}
					}
					System.out.print("\nEnter ID for assignment: " + ColorUtility.CYAN_TEXT);
					int assignId = Gradebook.sc.nextInt();
					Gradebook.sc.nextLine();
					
					if (assignId < assignmentsMissing.length && assignId > -1 && assignmentsMissing[assignId] > 0) {
						System.out.println(ColorUtility.YELLOW_TEXT + "\nEnter the amount for the grade [0-100]" + ColorUtility.CYAN_TEXT);
						int percentage = Gradebook.sc.nextInt();
						Gradebook.sc.nextLine();
						if (0 <= percentage && percentage <= 100) {
							Grade grade = new Grade();
							grade.setResult(percentage);
							grade.setCourse(found);
							grade.setTeacher(teacher);
							grade.setStudent(students.get(studId));
							grade.setCategory(Category.values()[assignId]);
							grade = gradeService.enterGrade(grade);
							students.get(studId).getGrades().add(grade);
							teacher.setGrades(gradeService.loadGradesTeacher(teacher));
							gradeEdit.populateGradeStudent(students.get(studId).getGrades());
							System.out.println(ColorUtility.GREEN_TEXT + "\nGrade Added!");
							
						} else {
							throw new IllegalOptionException();
						}
					} else {
						throw new IllegalOptionException();
					}
				} else {
					throw new IllegalOptionException();
				}
				do {
					System.out.println(ColorUtility.YELLOW_TEXT + "\nDo you want to add more grades? [Y/n]");
					String answer = Gradebook.sc.nextLine();
					if (answer.equalsIgnoreCase("y")) {
						break;
					} else if (answer.equalsIgnoreCase("n")) {
						return;
					} else {
						System.out.println(ColorUtility.RED_TEXT + "\nNot a valid answer. Try again!");
					}
				} while(true);
			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again!\n");
				Gradebook.sc.nextLine();
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again!\n");
			}
		} while(true);
		
	}

	private void sortGradeUtil(List<GradeUtil> grades) {
		if (sortMethod == "name") {
			Collections.sort(grades, new Comparator<GradeUtil>() {
				@Override
				public int compare(GradeUtil g1, GradeUtil g2) {
					return g1.getStudentName().compareTo(g2.getStudentName());
				}
			});
		} else {
			Collections.sort(grades, new Comparator<GradeUtil>() {
				@Override
				public int compare(GradeUtil g1, GradeUtil g2) {
					return g1.getCourseGrade() > g2.getCourseGrade() ? -1 : g1.getCourseGrade() == g2.getCourseGrade() ? 0 : 1;
				}
			});
		}
		
	}
	
	private double findMedian(List<GradeUtil> grades) {
		Collections.sort(grades, new Comparator<GradeUtil>() {
			@Override
			public int compare(GradeUtil g1, GradeUtil g2) {
				return g1.getCourseGrade() > g2.getCourseGrade() ? -1 : g1.getCourseGrade() == g2.getCourseGrade() ? 0 : 1;
			}
		});
		if (grades.size() % 2 == 0) {
			return ((grades.get(grades.size()/2).getCourseGrade() + grades.get(grades.size()/2 - 1).getCourseGrade()) / 2);
		} else {
			return grades.get(grades.size()/2).getCourseGrade();
		}
		
	}
	
	private double findAverage(List<GradeUtil> grades) {
		double average = 0;
		for (GradeUtil g : grades) {
			average += g.getCourseGrade();
		}
		return average / grades.size();
	}

}
